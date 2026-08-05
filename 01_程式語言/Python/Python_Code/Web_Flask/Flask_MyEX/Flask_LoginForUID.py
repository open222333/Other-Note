import os
from flask import Flask
from flask.helpers import url_for
from werkzeug.utils import redirect
from flask import request

app = Flask(__name__)

channelID = os.environ['LINE_LOGIN_CHANNEL_ID']
channelSecret = os.environ['LINE_LOGIN_CHANNEL_SECRET']
userID = os.environ['LINE_USER_ID']
callbackURL = os.environ['LINE_LOGIN_CALLBACK_URL']  # 例：https://<your-host>


@app.route("/")
def index():
    return redirect(url_for("getCodeLink"))


@app.route('/getCodeLink')
def getCodeLink():
    requestURL = 'https://access.line.me/oauth2/v2.1/authorize?'
    url = requestURL + 'response_type=code&client_id=' + \
        channelID + '&redirect_uri=' + callbackURL + \
        '&state=abcde&scope=profile%20openid'
    return redirect(url)


@app.route('/getCode', methods=['GET'])
def getCode():
    # 取得 code
    code = request.args.get('code')
    print(code)
    return code


@app.route("/test")
def test():
    return "Test"


if __name__ == "__main__":
    app.run(debug=True)
