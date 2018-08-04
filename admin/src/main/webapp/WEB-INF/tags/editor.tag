<%@ tag language="java" pageEncoding="UTF-8"%>

<%@ attribute name="textAreaName" required="true"%>
<%@ attribute name="getEditorContent" required="true"%>
<%@ attribute name="content"%>
<script src="${staticPath }/js/kindeditor-4.1.10/kindeditor-min.js"></script>
<script src="${staticPath }/js/kindeditor-4.1.10/lang/zh_CN.js"></script>

<script type="text/javascript">
	$(function() {
		var editor = KindEditor.create('textarea[name="${textAreaName}"]', {
			uploadJson : '${contextPath}/file/uploadImage',
			items:[
			        'source', '|', 'undo', 'redo', '|', 'preview', 'cut', 'copy', 'paste',
			        'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
			        'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
			        'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
			        'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
			        'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 
			         'table', 'hr', 'emoticons', 'pagebreak',
			        'anchor', 'link', 'unlink'
			]
		});
		
		${getEditorContent} = function(){
			// 同步数据后可以直接取得textarea的value
			editor.sync();
			var html = $('#editor_id').val(); // jQuery
			return html;
		}
	});
</script>

<textarea name="${textAreaName}" id="editor_id" style="width:800px;height:400px;visibility:hidden;">${content }</textarea>