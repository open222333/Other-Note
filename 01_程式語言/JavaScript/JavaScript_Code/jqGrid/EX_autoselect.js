$('#checkAll').click(function() {
  if (this.checked) {
    $("input[type=checkbox][name=ckjsmc]").each(function() {
      $(this).attr('checked', 'true');
    });
  } else {
    $("input[type=checkbox][name=ckjsmc]").each(function() {
      $(this).removeAttr('checked');
    });
  }
});

function checkOne() {
  var count = 0;
  $("input[type=checkbox][name=ckjsmc]").each(function() {
    if ($(this).attr('checked') != 'checked') { // 判斷一組複選框是否有未選中的 
      count += 1;
    }
  });
  if (count == 0) { // 如果沒有未選中的那麼全選框被選中
    $('#checkAll').attr('checked', 'true');
  } else {
    $('#checkAll').removeAttr('checked');
  }
}