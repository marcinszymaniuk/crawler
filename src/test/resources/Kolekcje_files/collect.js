(function(){var a,b,c,d,e,f;String.prototype.trim||(String.prototype.trim=function(){return this.replace(/^\s+|\s+$/g,"")}),e=function(a,b,c,d){return document.cookie=""+a+"="+b+";expires="+new Date((new Date).getTime()+c).toGMTString()+";path=/"+d},c=function(a){var b,c,d,e;for(a+="=",e=document.cookie.split(";"),c=0,d=e.length;d>c;c++)if(b=e[c],b=b.trim(),0===b.indexOf(a))return b.substring(a.length,b.length);return null},d=function(a,b){var c,e,f,g;f=[];for(e in a)g=a[e],c=b?b+"."+e:e,f.push("object"==typeof g?d(g,c):encodeURIComponent(c)+"="+encodeURIComponent(g));return f.join("&")},f=function(a){var b,c,d,e,f,g;return c="",d="",b=/[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,e={"\b":"\\b","	":"\\t","\n":"\\n","\f":"\\f","\r":"\\r",'"':'\\"',"\\":"\\\\"},f=function(a){return b.lastIndex=0,b.test(a)?'"'+a.replace(b,function(a){var b;return b=e[a],"string"==typeof b?b:"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)}):'"'+a+'"'},(g=function(a,b){var e,h,i,j,k,l;switch(i=c,l=b[a],l&&"object"==typeof l&&(l=l.valueOf()),typeof l){case"string":return f(l);case"number":return isFinite(l)?String(l):"null";case"boolean":case"null":return String(l);case"object":if(!l)return"null";if(c+=d,j=[],"[object Array]"===Object.prototype.toString.apply(l)){for(e in l)j.push(g(+e,l)||"null");return k=0===j.length?"[]":c?"[\n"+c+j.join(",\n"+c)+"\n"+i+"]":"["+j.join(",")+"]",c=i,k}for(h in l)Object.prototype.hasOwnProperty.call(l,h)&&(k=g(h,l),k&&j.push(f(h)+(c?": ":":")+k));return k=0===j.length?"{}":c?"{\n"+c+j.join(",\n"+c)+"\n"+i+"}":"{"+j.join(",")+"}",c=i,k}})("",{"":a})},a={bid:"_cmuid",clientVersion:"7",init:function(a){var b,c,d;if(this.conf=a,!this.conf.baseUrl)throw new CmInitException("$baseUrl of init object have to point to collector service accepting $GET requests");if(!this.conf.account)throw new CmInitException("account number is no set");return this.conf.baseUrl="/"===this.conf.baseUrl.slice(-1)?this.conf.baseUrl:this.conf.baseUrl+"/",this.conf.uid||(this.conf.uid="qeppo_priv_cookie"),this.conf.ses||(this.conf.ses="ws2"),this.conf.visitTimeSpan=null!=this.conf.visitTimeSpan?6e4*this.conf.visitTimeSpan:18e5,this.conf.domain=null!=this.conf.domain?";domain="+this.conf.domain:"",null==this.conf.pv&&(this.conf.pv=!0),null==(b=this.conf).callback&&(b.callback={}),this.conf.callback.onSuccess=this.conf.callback.onSuccess||function(){return null},this.conf.callback.onFailure=this.conf.callback.onFailure||function(){return null},null==(c=this.conf).responseTimeout&&(c.responseTimeout=3e3),this.conf.cookies||(this.conf.cookies=["ab_split","optimizelyBuckets"]),this.conf.pv&&this.pv(),this.trans={},this.deal={},"function"==typeof(d=this.conf).trans&&d.trans(),null!=window._cmEventsQueue&&this._flushEvents(window._cmEventsQueue),null!=window._cmPageViewsQueue&&this._flushPageViews(window._cmPageViewsQueue),window.cm},parseCallback:function(a){return null==a&&(a={}),a.onSuccess=a.onSuccess||this.conf.callback.onSuccess||null,a.onFailure=a.onFailure||this.conf.callback.onFailure||null,a},pv:function(a){var b;return a=this.parseCallback(a),b=this._gatherBrowserInfo(),this._addUid(b),this._addCustomParams(b,this.conf.customParams),this._addCookieValues(b,this.conf.cookies),this._addSessionId(b),this._pushToServer("c5t/pv",b,a.onSuccess,a.onFailure)},addDeal:function(a){return a._||(a._=(new Date).getTime()),this.deal[a.id]=a,delete a.id},submitDeal:function(a){return a=this.parseCallback(a),this._pushToServer("c5t/deal",{d:this.deal},a.onSuccess,a.onFailure)},event:function(a,b,c){return c=this.parseCallback(c),this._addUid(a),this._addCustomParams(a,b),this._pushToServer("c5t/event",a,c.onSuccess,c.onFailure)},_flushEvents:function(a){var b,c,d,e,f,g,h,i;for(i=[],f=0,g=a.length;g>f;f++){e=a[f],c={},h=e.pay;for(b in h)d=h[b],c[b]=d;c.ts=e.ts,i.push(this.event(c,e.cus,e.callback))}return i},_flushPageViews:function(a){var b,c,d,e;for(e=[],c=0,d=a.length;d>c;c++)b=a[c],e.push(this.pv(b.callback));return e},_addUid:function(a){var b;return a.uid="",this.conf.uid&&(b=c(this.conf.uid))?a.uid=b:void 0},_stringify:window.JSON&&window.JSON.stringify?window.JSON.stringify:f,_addCustomParams:function(a,b){var c;return a.cp="",b&&(c=this._stringify(b()))?a.cp=c:void 0},_addCookieValues:function(a,b){var d,e,f,g,h;if(a.c="",b){for(f={},g=0,h=b.length;h>g;g++)e=b[g],d=c(e),d&&(f[e]=d);if(!this._isEmpty(f))return a.c=this._stringify(f)}},_isEmpty:function(a){var b;if(a)for(b in a)return!1;return!0},_addSessionId:function(a){var b;return a.ses="",this.conf.ses&&(b=c(this.conf.ses))?a.ses=b:void 0},_gatherBrowserInfo:function(){var a;return{h:location.hostname,pth:0===location.pathname.indexOf("/")?location.pathname:"/"+location.pathname,qs:null!=location.search?location.search.replace("?",""):void 0,ref:document.referrer,res:null!==screen?screen.width+"x"+screen.height:null,ua:navigator.userAgent,ce:navigator.cookieEnabled?"1":"0",tz:-((new Date).getTimezoneOffset()/60),p:null!=(a=location.protocol)?a.replace(":",""):void 0,_:(new Date).getTime()}},_pushToServer:function(a,b,c,d){var e,f,g,h,i;for(c||(c=function(){return null}),d||(d=function(){return null}),b.ver=this.clientVersion,b.acc=this.conf.account,i=document.cookie.split(";"),g=0,h=i.length;h>g;g++)e=i[g],f=e.split("="),f[0].trim()===this.bid&&(b[this.bid]=f[1].trim());return this._compactJsonp(this.conf.baseUrl+a,b,c,d)},_countdownInterval:100,_countdown:function(a,b,c,d){var e,f;return f=this,e=c()?1:0,a>0?setTimeout(function(){return f._countdown(e?0:a-b,b,c,d),null},b):e||"function"!=typeof d||setTimeout(d,b),null},_registerEventListener:function(a,b,c){return"object"!=typeof a?!1:a.addEventListener?(a.addEventListener(b,c,!1),!0):a.attachEvent?(a.attachEvent("on"+b,c),!0):!1},_cmCallback:function(a){var b;return b=window.cm.bid,e(b,a[b],63072e6,window.cm.conf.domain),this._cleanup()},_scriptId:"cmjsonp",_cleanup:function(){var a;return a=document.getElementById(this._scriptId),null!==a?a.parentNode.removeChild(a):void 0},_compactJsonp:function(a,b,c,e){var f,g,h,i,j,k,l,m;if(m=!1,l=null,k=document.createElement("script"),j=document.getElementsByTagName("head")[0],!j)return!1;setTimeout(function(){return m=!0},3e3),i=function(){return(null===l||m===!0&&l!==!1)&&(l=!1,e()),null},k.onreadystatechange=function(){return function(){return"complete"!==k.readyState&&"loaded"!==k.readyState||null!==l||m!==!1?void 0:(l=!0,c())}}(this),k.onload=function(){return function(){return null===l&&m===!1?(l=!0,c()):void 0}}(this),k.type="text/javascript",k.async=!0,k.charset="utf-8",this._registerEventListener(k,"error",i),f=function(){return function(){return l===!0}}(this),this._countdown(this.conf.responseTimeout,this._countdownInterval,f,i),g="?"+d(b),k.setAttribute("src",a+g);try{j.appendChild(k)}catch(n){h=n,i()}return null},submitTrans:function(){return this._pushToServer("c5t/trans",{t:this.trans})},addTrans:function(a){return this.trans[a.id]=a,delete a.id,a.it={}},addItem:function(a){return this.trans[a.tId].it[a.id]=a,delete a.id,delete a.tId}};try{window._cm.scope||(window._cm.scope=window),window._cm.scope.cm=a,a.init(_cm)}catch(g){b=g,window._cm.scope.cm={pv:function(){},event:function(){},addTrans:function(){},addItem:function(){},addDeal:function(){},submitTrans:function(){},submitDeal:function(){}}}}).call(this);