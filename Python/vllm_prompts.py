from vllm import LLM, SamplingParams

def main():
    prompts = [
        """Provide some details about Flinders University. Go in depth"""
    ]

    sampling_params = SamplingParams(
        temperature=0.8,
        top_p=0.95,
        max_tokens=65536,
    )

    llm = LLM(
        model="deepseek-ai/DeepSeek-R1-Distill-Qwen-7B",
        #model="google/medgemma-27b-text-it",
        max_model_len=100000,
        gpu_memory_utilization=0.95,
        tensor_parallel_size=2,
        disable_custom_all_reduce=True
    )

    outputs = llm.generate(prompts, sampling_params)

    for output in outputs:
        full_text = output.outputs[0].text

        if "</think>" in full_text:
            answer = full_text.split("</think>", 1)[1].strip()
        else:
            answer = full_text.strip()

        print("===ANSWER_START===")
        print(answer)
        print("===ANSWER_END===")

if __name__ == "__main__":
    main()