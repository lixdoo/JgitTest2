(function($){
	var Gitinit = function() {
		var divModel = '<div class="table"> ' +
			'<svg class="octicon octicon-file-directory" width="14" viewBox="0 0 14 16" version="1.1" role="img" height="16" aria-hidden="true">'+
            'PATH'+
            '</svg>'+
            '</span> FILE'+
            '<span style="padding:33.333%">DATA</span>'+
            '<span style="padding-right:0">DATE</span>'+
            '</div>';
		var filePath = '<path d="M13 4H7v-1c0-0.66-0.31-1-1-1H1c-0.55 0-1 0.45-1 1v10c0 0.55 0.45 1 1 1h12c0.55 0 1-0.45 1-1V5c0-0.55-0.45-1-1-1z m-7 0H1v-1h5v1z">';
        var dirPath ='<path d="M6 5H2v-1h4v1zM2 8h7v-1H2v1z m0 2h7v-1H2v1z m0 2h7v-1H2v1z m10-7.5v9.5c0 0.55-0.45 1-1 1H1c-0.55 0-1-0.45-1-1V2c0-0.55 0.45-1 1-1h7.5l3.5 3.5z m-1 0.5L8 2H1v12h10V5z">';
		
        
        function checkButton(){
			   $.getJSON("../CheckButton/check",
			     null,	   
				 function(commits) {
					showGit(commits);
			   });
		}
		
		function showGit(data){
			$("#branches").html(data.branches.length);
			$("#commits").html(data.commits.length);
			$("#last-commit-objectid").html(data.commitId.substring(0, 6));
			$("#last-commit-author").html(data.author);
			$("#last-commit-content").html(data.content);
			$("#date").html(data.date);
			bindData(data);
			skipPage(data);
		}
		function bindData(data){
			var length = data.fileName.length;
			for(var i = 0;i<length;i++){
				var div = divModel;
				if(data.fileType[i]=='Directory'){
					div = div.replace(/PATH/g,filePath);
				}else{
					div = div.replace(/PATH/g,dirPath);
				}
				div=div.replace(/FILE/g,data.fileName[i]);
				div=div.replace(/DATA/g, 'int');
				div=div.replace(/DATE/g,'2013.01.02');
				$(".stats-switcher-wrapper").append(div);
			}
			
			
		}
		function skipPage(){
			location.href = "FileContent.html";
		}
		
	    this.init=function(){
			$("#CheckButton").on("click",checkButton);
			$(".aClick").on("click",skipPage);
	   };
	};
	
	$(document).ready(function(){
		var loginer = new Gitinit();  
		loginer.init();
	});

})(jQuery) ;







