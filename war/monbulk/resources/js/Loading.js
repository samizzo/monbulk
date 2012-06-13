var __loadingWrapper = null;

function beginLoading(project,title) {
   __loadingWrapper = document.createElement("div");
    
    var loading = document.createElement("div");
    loading.style.background = "white";
    loading.style.border = "1px solid #ccc";
    loading.style.position = "absolute";
    loading.style.left = "45%";
    loading.style.top = "40%";
    loading.style.padding = "2px";
    loading.style.zIndex = "1000000";
    loading.style.height = "auto";
    loading.style.borderRadius = "5px";
    loading.style.MozBorderRadius = "5px";
    loading.style.boxShadow = "2px 3px 20px 5px rgba(0,0,0,0.5)";
    loading.style.MozBoxShadow = "2px 3px 20px 5px rgba(0,0,0,0.5)";
    loading.style.webkitBoxShadow = "2px 3px 10px 5px rgba(0,0,0,0.5)";
    loading.style.opacity = "0.95";

    __loadingWrapper.appendChild(loading);

    var loadingIndicator = document.createElement("div");
    loadingIndicator.style.font = "bold 13px helvetica, tahoma, arial";
    loadingIndicator.style.padding = "10px";
    loadingIndicator.style.margin = "0px";
    loadingIndicator.style.height = "auto";
    loadingIndicator.style.color = "#444";

    loading.appendChild(loadingIndicator);

    var loadingTitle = document.createElement("div");
    loadingIndicator.appendChild(loadingTitle);
    
    var img = document.createElement("img");
    img.src = project + "/resources/images/busy.gif";
    img.style.width = "16px";
    img.style.height = "16px";
    img.style.marginRight = "8px";
    img.style.float = "left";
    img.style.verticalAlign = "top";
    loadingTitle.appendChild(img);
    loadingTitle.appendChild(document.createTextNode(title));

    var loadingMsg = document.createElement("div");
    loadingMsg.style.font = "normal 10px helvetica, arial, tahoma, sans-serif";
    loadingMsg.style.marginTop = "20px";
    loadingMsg.style.textAlign = "center";

    loadingMsg.innerHTML = 'Loading Application<br/>Please wait ...';
    loadingIndicator.appendChild(loadingMsg);

    document.body.appendChild(__loadingWrapper);
	
}

function endLoading() {
    if ( !__loadingWrapper ) {
	return;
    }
	
    document.body.removeChild(__loadingWrapper);

    __loadingWrapper = null;
}

