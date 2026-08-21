import os
import subprocess
from pathlib import Path
from fastapi import FastAPI, UploadFile, File, BackgroundTasks

UPLOAD_DIR_DSTACK = Path("/home/icaadmin/fileuploads")
UPLOAD_DIR_VLLM = Path("/home/icaadmin/vllm")

DSTACK_BIN = "/home/icaadmin/dstackvirt/bin/dstack"
VLLM_BIN = "/home/icaadmin/vllmvirt/bin/python"
app = FastAPI()

def dstack(config_path: Path):
    result = subprocess.run(
        [DSTACK_BIN, "apply", "-f", str(config_path), "-y"],
        cwd = config_path.parent,
        capture_output = True,
        text = True,
    )

    if result.returncode != 0:
        print(f"[dstack apply] FAILED for {config_path}")
        print(result.stderr)
    else:
        print(f"[dstack apply] SUCCESS for {config_path}")
        print(result.stdout)

    return {
        "returncode": result.returncode,
        "stdout": result.stdout,
        "stderr": result.stderr,
    }

def vllm(config_path: Path):

    env = os.environ.copy()
    env["PATH"] = (
            "/home/icaadmin/vllmvirt/bin:"
            + env.get("PATH", "")
    )
    env["NCCL_P2P_DISABLE"] = "1"

    result = subprocess.run(
        [VLLM_BIN, str(config_path)],
        env=env,
        cwd=config_path.parent,
        capture_output=True,
        text=True,
    )

    if result.returncode != 0:
        print(f"vllm FAILED for {config_path}")
        print(result.stderr)
    else:
        print(f"vllm SUCCESS for {config_path}")
        print(result.stdout)

    answer = ""
    if "===ANSWER_START===" in result.stdout and "===ANSWER_END===" in result.stdout:
        answer = result.stdout.split("===ANSWER_START===")[1].split("===ANSWER_END===")[0].strip()

    return {
        "returncode": result.returncode,
        "answer": answer,
        "stderr": result.stderr,
    }

@app.post("/dstack")
async def upload(file: UploadFile = File(...)):
    safe_name = Path(file.filename).name
    destination = UPLOAD_DIR_DSTACK / safe_name

    with open(destination, "wb") as f:
        f.write(await file.read())

    apply_result = dstack(destination)

    return {"uploaded": safe_name, "dstack": apply_result}

@app.post("/vllm")
async def upload(file: UploadFile = File(...)):
    safe_name = Path(file.filename).name
    destination = UPLOAD_DIR_VLLM / safe_name

    with open(destination, "wb") as f:
        f.write(await file.read())

    apply_result = vllm(destination)

    return {"uploaded": safe_name, "vllm": apply_result}

# make separate @app.post(/dstack, vllm etc)
# make them do different things in the body itself