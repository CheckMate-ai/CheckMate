import requests


API_URL = "https://api-inference.huggingface.co/models/mistralai/Mixtral-8x7B-Instruct-v0.1"
headers = {"Authorization": "Bearer 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"}


class State:
    def __init__(self):
        self.context = """AI: Hello.
               I have one main function:
               Extract five key words that summarize the main points of a given text at this format {"result" : [keywords]}
            """
        self.conversation = {
            "Conversation": []
        }
        self.lastAImessage = ""

    def __str__(self):
        return f"Context: {self.context}\nConversation: {self.conversation}\nLast AI message: {self.lastAImessage}"


def query(payload):
    response = requests.post(API_URL, headers=headers, json=payload)
    return response.json()


def request(prompt: str) -> str:
    output = query(
        {
            "inputs": prompt,
        }
    )
    return output[0]["generated_text"]
    """from gradio_client import Client
    client = Client("Bibi210/CheckMate-Gemma")
    job = client.submit(
        prompt,  # str  in 'Text' Textbox component
        api_name="/predict"
    )
    return job.result() """


def send_message(state: State, msg: str) -> None:
    """
    Send the user's message to the API and update the conversation.

    Args:
        - state: The current state of the app.
    """
    # Add the user's message to the context
    state.context += f"Human: \n{msg}\n\nAI:"
    # Send the user's message to the API and get the response
    answer = request(state.context).replace(state.context, "")
    state.lastAImessage = answer
    # Add the response to the context for future messages
    state.context += answer
    # Update the conversation
    conv = state.conversation
    conv["Conversation"] += [msg, answer]
    state.conversation = conv


def main():
    state = State()
    statement = "Kilian Mbappe the best football player in the world will sign for Manchester United in the summer of 2024"
    msg = f"Here is a statement: {statement}\ngive me key words."
    send_message(state, msg)
    print(state.lastAImessage)


main()
