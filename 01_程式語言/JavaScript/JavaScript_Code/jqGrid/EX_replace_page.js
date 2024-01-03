var countsCheckBox = $("input[type='checkbox']:checked");
var booksid = [];
for (var i = 0; i < countsCheckBox.length; i) {
  //使用[]取得元素是是一個domElement元素，取值需要使用.value， 
  //如果使用countsCheckBox.eq(i) 則是一個Obkject元素，就可以使用val()取值 
  //alert(countsCheckBox[i].value); 
  mysendbook_id = {};
  mysendbook_id['book_id'] = countsCheckBox[i].value;
  booksid[i] = mysendbook_id;
}
//alert(booksid); 
var confirmdel = confirm('確認要刪除嗎?');
if (confirmdel) {
  //開始請求刪除 
  $.ajax({
    url: 'selectdelbooks',
    data: JSON.stringify(booksid),
    type: 'post',
    success: function(res) {
      alert("刪除成功");
      location.replace("/TheDemo/books/pageBooksShow");
    }
  });
}