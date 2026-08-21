# setting time and date
sudo timedatectl set-ntp true
sudo systemctl restart chrony
#makestep 1.0 3
date
sleep 1
date
sleep 1
date
sleep 1
date 
sleep 1
date 
sleep 1
date
sleep 1
date
sleep 1
date
sleep 1
date
sleep 1
date
sleep 1
date

sleep 30

sudo apt update && sudo apt upgrade -y
sudo ubuntu-drivers install

# installing latest python and utils
sudo apt install python3-full -y
sudo apt install mesa-utils -y

sleep 30

# docker engine install
sudo apt remove $(dpkg --get-selections docker.io docker-compose docker-compose-v2 docker-doc podman-docker containerd runc | cut -f1)
sudo apt update
sudo apt install ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc
sudo tee /etc/apt/sources.list.d/docker.sources <<EOF
Types: deb
URIs: https://download.docker.com/linux/ubuntu
Suites: $(. /etc/os-release && echo "${UBUNTU_CODENAME:-$VERSION_CODENAME}")
Components: stable
Architectures: $(dpkg --print-architecture)
Signed-By: /etc/apt/keyrings/docker.asc
EOF
sudo apt update
sudo apt install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin -y

sleep 30

# installing dstack with docker compose file
sudo mkdir dstackdocker
cd ~/dstackdocker
sudo tee docker-compose.yml > /dev/null <<'EOF'
services:
  dstack-server:
    image: dstackai/dstack
    container_name: my-test-dstack-server
    restart: always
    ports:
      - "3000:3000"
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
      - ~/.dstack/server:/root/.dstack/server
EOF
cd ~/dstackdocker
sudo docker compose up -d

sleep 30

#installing python 3.13 for dstack virtual environment
cd ~
sudo apt update && sudo apt upgrade -y
sudo apt install software-properties-common -y
sudo add-apt-repository ppa:deadsnakes/ppa -y
sudo apt update
sudo apt install python3.13 python3.13-full python3.13-venv python3.13-dev -y

sleep 30

# creating virutal environment to manage dstack
python3.13 -m venv .venv
source .venv/bin/activate
python --version

sleep 30

#installing pip in virt
sudo apt update
sudo apt install python3-pip -y

sleep 30

# installing dstack in virt
python -m pip install dstack
dstack project add --name main --url http://icaserver.icadl.au:3000 --token 99de9465-a762-4f1f-8d1e-55bcdff504d0
dstack fleet
deactivate

sleep 30

# installing local dns server
cd
mkdir -p dnsdocker/config
cd dnsdocker
echo "10.5.20.3 icaserver.icadl.au" > config/hosts
cat << EOF > config/dnsmasq.conf
log-queries
addn-hosts=/etc/dnsmasq/hosts
server=1.1.1.1
server=8.8.8.8
EOF
cat << EOF > docker-compose.yml
services:
  dnsmasq:
    image: strm/dnsmasq:latest
    container_name: dns_server
    restart: unless-stopped
    ports:
      - "53:53/udp"
      - "53:53/tcp"
    volumes:
      - ./config/dnsmasq.conf:/etc/dnsmasq.conf:ro
      - ./config/hosts:/etc/dnsmasq.hosts:ro
    cap_add:
      - NET_ADMIN
EOF
cd
echo "DNSStubListener=no" | sudo tee -a /etc/systemd/resolved.conf
sudo systemctl restart systemd-resolved
cd dnsdocker
sudo docker compose up -d --force-recreate
sudo docker ps

#sleep 30

#sudo reboot