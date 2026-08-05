import os
import requests


def apiAuthentication(request):
    pass


def getAuthorizeCode():
    url = 'https://notify-bot.line.me/oauth/authorize'
    # headers = {
    #     'Content-Type': 'application/-www-form-urlencoded',
    # }
    parameters = {
        'response_type': 'code',
        'client_id': os.environ['LINE_NOTIFY_CLIENT_ID'],
        'redirect_uri': 'https://www.pureentertainment.cc/bot/callback',
        'scope': 'notify',
        'state': 'NO_STATE',
        'response_mode': 'form_post',
    }
    result = requests.get(url, params=parameters)
    return result.url


def postForToken():
    url = 'https://notify-bot.line.me/oauth/token'
    client_secret = os.environ['LINE_NOTIFY_CLIENT_SECRET']
    headers = {
        'Content-Type': 'application/x-www-form-urlencoded',
    }
    parameters = {
        'grant_type': 'authorization_code',
        'code': getAuthorizeCode(),
        'redirect_uri': 'https://www.pureentertainment.cc/bot/callback',
        'client_id': os.environ['LINE_NOTIFY_CLIENT_ID'],
        'client_secret': client_secret,
    }
    result = requests.post(url, headers=headers, params=parameters)
    return result.text


# print(getAuthorizeCode())
print(postForToken())
