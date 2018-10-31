/* 
 * detail JavaScript
 * write by liulei 20141007
 */
/* 文档加载完毕后 */
$(document).ready(function() {
	docReady();
	//	var jsonStr = $("#data").val();
	//	var json = eval("(" + jsonStr + ")");
	//	initHtml(json,1);
});


function initHtml(json, fontSize) {
	renderHtml(json, {
		ret: 1
	}, false);

	setFontSize(fontSize);
}

//阻止浏览器的默认行为
function stopDefault(e) {
		//阻止默认浏览器动作(W3C)
		if (e && e.preventDefault)
			e.preventDefault();
		//IE中阻止函数器默认动作的方式
		else
			window.event.returnValue = false;
		return false;
	}
	/** 
	 * 对日期进行格式化，
	 * @param date 要格式化的日期
	 * @param format 进行格式化的模式字符串
	 *     支持的模式字母有：
	 *     y:年,
	 *     M:年中的月份(1-12),
	 *     d:月份中的天(1-31),
	 *     h:小时(0-23),
	 *     m:分(0-59),
	 *     s:秒(0-59),
	 *     S:毫秒(0-999),
	 *     q:季度(1-4)
	 * @return String
	 * @author yanis.wang
	 * @see	http://yaniswang.com/frontend/2013/02/16/dateformat-performance/
	 */
template.helper('dateFormat', function(date, format) {
	date = new Date(parseInt(date) * 1000);
	var map = {
		"M": date.getMonth() + 1, //月份 
		"d": date.getDate(), //日 
		"h": date.getHours(), //小时 
		"m": date.getMinutes(), //分 
		"s": date.getSeconds(), //秒 
		"q": Math.floor((date.getMonth() + 3) / 3), //季度 
		"S": date.getMilliseconds() //毫秒 
	};
	format = format.replace(/([yMdhmsqS])+/g, function(all, t) {
		var v = map[t];
		if (v !== undefined) {
			if (all.length > 1) {
				v = '0' + v;
				v = v.substr(v.length - 2);
			}
			return v;
		} else if (t === 'y') {
			return (date.getFullYear() + '').substr(4 - all.length);
		}
		return all;
	});
	return format;
});

template.helper('dateFormatComment', function(date, format) {
	var unix_time = get_unix_time(date);
	date = new Date(parseInt(unix_time) * 1000);
	var dateTime = "";
	var minute = 1000 * 60;
	var hour = minute * 60;
	var day = hour * 24;
	var halfamonth = day * 15;
	var month = day * 30;

	function getDateDiff(dateTimeStamp) {
		var now = new Date().getTime();
		var diffValue = now - dateTimeStamp;
		var monthC = diffValue / month;
		var weekC = diffValue / (7 * day);
		var dayC = diffValue / day;
		var hourC = diffValue / hour;
		var minC = diffValue / minute;
		if (monthC >= 1) {
			result = (date.getFullYear() + '').substr(2) + " / " + pad(date.getMonth() + 1, 2) + " / " + pad(date.getDate(), 2);
		} else if (weekC >= 1) {
			result = parseInt(weekC) + "周前";
		} else if (dayC >= 1) {
			result = parseInt(dayC) + "天前";
		} else if (hourC >= 1) {
			result = parseInt(hourC) + "个小时前";
		} else if (minC >= 1) {
			result = parseInt(minC) + "分钟前";
		} else
			result = "刚刚";
		return result;
	}

	dateTime = getDateDiff(date);
	return dateTime;

});

template.helper('getYMD', function(data) {
	if (data && data.length >= 10)
		return data.substring(0, 10);
});


function get_unix_time(dateStr) {
	var newstr = dateStr.replace(/-/g, '/');
	var date = new Date(newstr);
	var time_str = date.getTime().toString();
	return time_str.substr(0, 10);
}

function getDays(strDateStart, strDateEnd) {
	var strSeparator = "-"; //日期分隔符
	var oDate1;
	var oDate2;
	var iDays;
	oDate1 = strDateStart.split(strSeparator);
	oDate2 = strDateEnd.split(strSeparator);
	var strDateS = new Date(oDate1[0] + "-" + oDate1[1] + "-" + oDate1[2]);
	var strDateE = new Date(oDate2[0] + "-" + oDate2[1] + "-" + oDate2[2]);
	iDays = parseInt(Math.abs(strDateS - strDateE) / 1000 / 60 / 60 / 24) //把相差的毫秒数转换为天数 
	return iDays;
}

function pad(num, n) {
	var len = num.toString().length;
	while (len < n) {
		num = "0" + num;
		len++;
	}
	return num;
}


//文档加载完成
function docReady() {

		//所有#链接取消默认动作
		$("body").on("click", "a[href='#']", function(e) {
			stopDefault(e);
		})
	}
	//渲染正文

function renderDetail(data) {
	var html = template('tmpDetail', data);
	$('#htmlDetail').html(html);
	$(".detail img").css({
		"width": "100%",
		"height": "auto"
	});
	$(".detail p").css({
		"font-size": "auto"
	});

	bindImg();
}

function bindImg() {
	//绑定图片跳转大图
	$("#htmlDetail,#htmlMedias,#newsImgs").on("click", 'img', function(e) {
		var url = $(this).attr("src");
		jsObj.checkBigPic(url);
	})
}


function renderHeader(data) {
	var html = template("tmpHeader", data);
	$("#htmlHeader").html(html);
}


function renderImgs(data) {
	var imgStr = data.images;
	var imgInfo = data.imginfo;
	var imgArray = imgStr.split(';');
	var infoArray = imgInfo.split('|');
	var imgs = new Array();
	for (var i = 0; i < imgArray.length; i++) {
		if (imgArray[i] != "" && infoArray[i] != "") {
			var json = {
				url: imgArray[i],
				info: infoArray[i]
			};
			imgs.push(json)
		} else if (imgArray[i] != "") {
			var json = {
				url: imgArray[i],
				info: ""
			};
			imgs.push(json)
		}
	}
	var json = {
		imgs: imgs
	};
	var html = template('imgs', json);
	$("#newsImgs").html(html);
}

//字体切换
function setFontSize(size) {
	if (size == 0) {
		$("body").attr("class", "font_big");
	} else if (size == 1) {
		$("body").attr("class", "font_middle");
	} else if (size == 2) {
		$("body").attr("class", "font_min");
	}
}

function renderHtml(json, fontSize) {
	renderHeader(json.data);
	renderImgs(json.data);
	renderDetail(json.data)

	setTimeout(function() {
		$("#content").fadeIn("normal").css("opacity", "1");
		//jsObj.hideLoading();
	});
}

function share(platform){
	jsObj.share(platform);
}
