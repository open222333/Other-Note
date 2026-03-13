// https://ithelp.ithome.com.tw/articles/10251756?sc=rss.iron
import $ from "jquery"


export const Model = () => {
  //1.預設處理畫面中原有modal
  if (removeModal) {
    if (document.querySelectorAll('[data-modal="true"]').length) {
      document
        .querySelectorAll('[data-modal="true"]')
        .forEach((v) => v.remove())
    }
  }

  //2.建立modal物件
  const modal = document.createElement('div')
  //賦予modal ID
  modal.id = id
  //賦予modal class
  modal.className = 'modal fade'
  modal.setAttribute('data-modal', 'true')
  modal.setAttribute('data-backdrop', 'static')

  modal.innerHTML = `
  <div class="modal-dialog">
    <div class="modal-content">
      ${content}
    </div>
  </div>
  `

  //3.新增至DOM
  document.querySelector('body').appendChild(modal)

  //4.執行跳出modal
  $(`#${id}`).modal('show')

  //5.顯示時執行的回呼函式
  $(`#${id}`).on('shown.bs.modal', function (e) {
    callback ? callback() : null
  })  

  //6.隱藏時移除modal
  $(`#${id}`).on('hidden.bs.modal', function (e) {
    document.querySelector(`#${id}`).remove()
  })
  
}