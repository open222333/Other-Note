// html form表單 處理範例

/*
範例
<div class="btn-group">
	<label class="btn btn-default btn-sm">
		<form id="batch_change_domain">
			<span>一鍵修改域名  </span>
			<span>舊域名:</span>
			<input type="text" name="old_domain" />
			<span>新域名:</span>
			<input type="text" name="new_domain" />
			<input type="submit" class="btn btn-sm btn-success" value="更換" />
		</form>
	</label>
</div>
*/

$('#batch_change_domain').submit(function () {
  let old_domain = $("input[name=old_domain]").val();
  let new_domain = $("input[name=new_domain]").val();
  $.ajax({
    method: 'post',
    url: 'batch_change_domain',
    data: {
      _token: LA.token,
      old_domain: old_domain,
      new_domain: new_domain
    },
    success: function (response) {
      if (response['message'] == 'ok') {
        toastr.success('一鍵更換域名操作成功');
        // $.pjax.reload('#pjax-container');
		
      }
      else {
        toastr.error(response['message']);
      }
    },
    error: function () {
      toastr.error('一鍵更換域名操作失敗');
    },
  });
})