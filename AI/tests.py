import requests

def llama_chain():
  
    api_url = 'https://dk8vc67v5b.execute-api.eu-west-2.amazonaws.com/default/TestFunc'

    json ={
        "inputs": [
            [
                {
                    "role": "system",
                    "content": "You are an expert in copywriting"
                },
                {
                    "role": "user",
                    "content": "Write me a tweet about super conductors"
                }
            ]
        ],
        "parameters": {
            "max_new_tokens": 256,
            "top_p": 0.9,
            "temperature": 0.6
        }
    }

    r = requests.post(api_url, json = json)
    """ answer = r.json()[0]["generation"] """
    return r.json()

print(llama_chain())