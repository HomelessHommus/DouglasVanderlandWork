import meraki
import requests

def get_meraki_ip(api_key, organization_id, serial_number):

    # uses getOrganizationDevicesUplinksAddressesByDevice api call to get info
    dashboard = meraki.DashboardAPI(api_key)
    response = dashboard.organizations.getOrganizationDevicesUplinksAddressesByDevice(
        organization_id,
        serials=serial_number)

    # isolates response to just the public IP
    for z3 in response:
        for uplink in z3.get("uplinks", []):
            if uplink.get('interface') != 'wan1':
                continue
            for address in uplink.get('addresses', []):
                publicIpAddress = address.get('address')
                if publicIpAddress:
                    print("New IP: " + publicIpAddress)
                    return publicIpAddress

def change_dns_ip(api_key, api_secret, new_ip):

    #send api request to url with domain icadl.au and host icasever
    response = requests.put(
        url="https://api.godaddy.com/v1/domains/icadl.au/records/A/icaserver",
        json=[{"data": new_ip, "ttl": 1800}],
        headers={
            "Authorization": f"sso-key {api_key}:{api_secret}",
            "Content-Type": "application/json",
        },
    )

    if response.status_code != 200:
        print("IP change failed. Status code: ", response.status_code)
    else:
        print("IP changed for icaserver.icadl.au")

def main():

    # temp storage of important info
    API_KEY_MERAKI = "REDACTED"
    organizationID = "REDACTED"
    serialNum = "REDACTED"

    API_KEY_GODADDY = "REDACTED"
    API_SECRET_GODADDY = "REDACTED"

    newPublicIp = get_meraki_ip(API_KEY_MERAKI, organizationID, serialNum)
    change_dns_ip(API_KEY_GODADDY, API_SECRET_GODADDY, newPublicIp)

if __name__ == "__main__":
    main()

# TO FIX REMOVE CNAME IN GODADDY AND CHANGE TO A REGULAR IP ADDRESS
# CURRENTLY DOES NOT WORK