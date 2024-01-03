grid.jqGrid('setGroupHeaders', {
  depth: 3,
  useColSpanStyle: true,
  groupHeaders: headers
});


(function() {
  $.fn.jqGrid.setGroupHeaders = function(o) {
    o = $.extend({
      useColSpanStyle: false,
      depth: 2,
      groupHeaders: []
    }, o || {});
    return this.each(function() {
      this.p.groupHeader = o;
      var ts = this,
        i, cmi, skip = 0,
        $tr, $colHeader, th, $th, thStyle,
        iCol,
        cghi,
        //startColumnName,
        numberOfColumns,
        titleText,
        cVisibleColumns,
        colModel = ts.p.colModel,
        cml = colModel.length,
        ths = ts.grid.headers,
        $htable = $("table.ui-jqgrid-htable", ts.grid.hDiv),
        $trLabels = $htable.children("thead").children("tr.ui-jqgrid-labels:last").addClass("jqg-second-row-header"),
        $thead = $htable.children("thead"),
        $theadInTable,
        $firstHeaderRow = $htable.find(".jqg-first-row-header");
      if ($firstHeaderRow[0] === undefined) {
        $firstHeaderRow = $('<tr>', {
          role: "row",
          "aria-hidden": "true"
        }).addClass("jqg-first-row-header").css("height", "auto");
      } else {
        $firstHeaderRow.empty();
      }
      var $firstRow,
        inColumnHeader = function(text, columnHeaders) {
          var length = columnHeaders.length,
            i;
          for (i = 0; i < length; i++) {
            if (columnHeaders[i].startColumnName === text) {
              return i;
            }
          }
          return -1;
        };

      $(ts).prepend($thead);
      $tr = $('<tr>', {
        role: "rowheader"
      }).addClass("ui-jqgrid-labels jqg-third-row-header");
      for (i = 0; i < cml; i++) {
        th = ths[i].el;
        $th = $(th);
        cmi = colModel[i];
        // build the next cell for the first header row
        thStyle = {
          height: '0px',
          width: ths[i].width + 'px',
          display: (cmi.hidden ? 'none' : '')
        };
        $("<th>", {
          role: 'gridcell'
        }).css(thStyle).addClass("ui-first-th-" + ts.p.direction).appendTo($firstHeaderRow);

        th.style.width = ""; // remove unneeded style
        iCol = inColumnHeader(cmi.name, o.groupHeaders);
        if (iCol >= 0) {
          cghi = o.groupHeaders[iCol];
          numberOfColumns = cghi.numberOfColumns;
          titleText = cghi.titleText;

          // caclulate the number of visible columns from the next numberOfColumns columns
          for (cVisibleColumns = 0, iCol = 0; iCol < numberOfColumns && (i + iCol < cml); iCol++) {
            if (!colModel[i + iCol].hidden) {
              cVisibleColumns++;
            }
          }

          // The next numberOfColumns headers will be moved in the next row
          // in the current row will be placed the new column header with the titleText.
          // The text will be over the cVisibleColumns columns
          $colHeader = $('<th>').attr({
              role: "columnheader"
            })
            .addClass("ui-state-default ui-th-column-header ui-th-" + ts.p.direction)
            .css({
              'height': '22px',
              'border-top': '0 none'
            })
            .html(titleText);
          if (cVisibleColumns > 0) {
            $colHeader.attr("colspan", String(cVisibleColumns));
          }
          if (ts.p.headertitles) {
            $colHeader.attr("title", $colHeader.text());
          }
          // hide if not a visible cols
          if (cVisibleColumns === 0) {
            $colHeader.hide();
          }

          $th.before($colHeader); // insert new column header before the current
          $tr.append(th); // move the current header in the next row

          // set the coumter of headers which will be moved in the next row
          skip = numberOfColumns - 1;
        } else {
          if (skip === 0) {
            if (o.useColSpanStyle) {
              // expand the header height to two rows
              $th.attr("rowspan", o.depth + "");
            } else {
              $('<th>', {
                  role: "columnheader"
                })
                .addClass("ui-state-default ui-th-column-header ui-th-" + ts.p.direction)
                .css({
                  "display": cmi.hidden ? 'none' : '',
                  'border-top': '0 none'
                })
                .insertBefore($th);
              $tr.append(th);
            }
          } else {
            // move the header to the next row
            //$th.css({"padding-top": "2px", height: "19px"});
            $tr.append(th);
            skip--;
          }
        }
      }
      $theadInTable = $(ts).children("thead");
      $theadInTable.prepend($firstHeaderRow);
      $tr.insertAfter($trLabels);
      $htable.append($theadInTable);

      if (o.useColSpanStyle) {
        // Increase the height of resizing span of visible headers
        $htable.find("span.ui-jqgrid-resize").each(function() {
          var $parent = $(this).parent();
          if ($parent.is(":visible")) {
            this.style.cssText = 'height: ' + $parent.height() + 'px !important; cursor: col-resize;';
          }
        });

        // Set position of the sortable div (the main lable)
        // with the column header text to the middle of the cell.
        // One should not do this for hidden headers.
        $htable.find("div.ui-jqgrid-sortable").each(function() {
          var $ts = $(this),
            $parent = $ts.parent();
          if ($parent.is(":visible") && $parent.is(":has(span.ui-jqgrid-resize)")) {
            $ts.css('top', ($parent.height() - $ts.outerHeight()) / 2 + 'px');
          }
        });
      }

      $firstRow = $theadInTable.find("tr.jqg-first-row-header");
      $(ts).bind('jqGridResizeStop.setGroupHeaders', function(e, nw, idx) {
        $firstRow.find('th').eq(idx).width(nw);
      });
    });
  }
})();