# Python 模組 Flask-WTF wtforms(表單)

```
Forms表單是WTForms最主要的核心容器。

Forms表單是Fields欄位的集合，可以選擇透過字典（dictionary）或屬性（attribute）的方式來接觸它們。

Fields則將大部分粗重的工作攬在身上。

每個Fields欄位都代表某一種資料的類型，且Fields欄位限制使用者僅能夠輸入符合該資料類型的數據。

舉例來說IntegerField與StringField代表的是兩種不一樣的資料型態，一個是數字而另一個則是字串。

此外，Fields欄位除包含數據之外，還包含其他有用的屬性，例如label（標籤），description（描述）和錯誤驗證等。

每個Fields欄位都有一個widget實例。

widget的工作是呈現該Fields欄位對應的HTML標籤。

你可以指定Widget實例給每個特定的Fields欄位，不過在預設的情況下每個Fields欄位都有一個widget實例。

有一些Fields欄位設置的目的只是為了工程師使用上方便，舉例來說TextAreaField只是一個字串欄位（StringField），而預設的widget是TextArea。

為了提供各種驗證規則，Fields欄位包含了一系列的驗證方式。


Flask-WTF
Flask 和 WTForms 的簡單集成，包括 CSRF、文件上傳和 reCAPTCHA。
```

## 目錄

- [Python 模組 Flask-WTF wtforms(表單)](#python-模組-flask-wtf-wtforms表單)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [中文範例](#中文範例)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[wtforms pypi](https://pypi.org/project/wtforms/)

[Flask-WTF pypi](https://pypi.org/project/Flask-WTF/)

[WTForms - 官方文檔](https://wtforms.readthedocs.io/en/3.0.x/)

[Flask-WTF - 官方文檔](https://flask-wtf.readthedocs.io/en/1.0.x/)

### 中文範例

[Python Web Flask — 用WTF Form製作表單](https://medium.com/seaniap/python-web-flask-%E7%94%A8wtf-form%E8%A3%BD%E4%BD%9C%E8%A1%A8%E5%96%AE-1f4af213ea88)

# 指令

```bash
# 安裝 wtforms
pip install wtforms

# 安裝 Flask-WTF
pip install Flask-WTF
```

# 用法

```Python
from flask_wtf import FlaskForm
from wtforms import StringField, SubmitField
from wtforms.validators import DataRequired


# 建立表單
class RegForm(FlaskForm):
	'''使用者登入'''
    username  = StringField('username',validators=[DataRequired()])
    submit = SubmitField("Submit")
```

```Python
# 建立路由
@app.route('/', methods=['GET','POST'])
def index():
    """首頁"""
    username = False
    #form為類別的實體
    form = RegForm()
    if form.validate_on_submit():
        #取出username欄位的輸入值
        username = form.username.data
        #重設username欄位
        form.username.data = ''
    #將username與form帶入首頁home.html樣板中
    return render_template('home.html', form=form,username=username)
```

```html
<form method="POST">
    {{form.hidden_tag()}}
    {{form.username.label}} {{form.username()}}
    {{form.submit()}}
</form>
```