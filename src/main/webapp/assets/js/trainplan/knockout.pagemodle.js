function PageModle(pageSize, fun){
	var self = this; 
	
//	<ul id="page_footer_ul" class="pagination pull-right" style="margin: 0px; display: block;">
//	<li class="disabled"><a>?</a></li>
//	<li class="active"><a>1
//	<span class="sr-only"></span></a></li>
//	<li><a style="cursor:pointer" onclick="switchToPage(2)">2</a></li>
//	<li><a style="cursor:pointer" onclick="switchToPage(3)">3</a></li>
//	<li><a style="cursor:pointer" onclick="switchToPage(4)">4</a></li>
//	<li><a style="cursor:pointer" onclick="switchToPage(5)">5</a></li>
//	<li><a style="cursor:pointer" onclick="switchToPage(5)">?</a></li>
//	</ul>
	 
	self.loadFunc = fun;
	
	self.currentIndex = ko.observable(0);
	
	self.pageSize = ko.observable(pageSize); 
	
	self.currentPage = ko.observable(0);
	
	self.totalCount = ko.observable(0);
	
	self.rows = ko.observableArray();  
	
	self.endIndex = ko.observable(0); 
	
	self.currentPage = ko.observable(0); 
	
	self.pageCount = ko.observable(0); 
	

	self.getPageEndIndex = function(){
	
		return self.currentIndex() + self.pageSize();
	};
	self.loadPre = function(){
//		console.log("loadPre")
//		if(self.currentPage()*self.pageSize() > self.totalCount()){
//			
//		}
//		var currentIndex = self.currentIndex();   
//		if(currentIndex == 0){
//			return;
//		}
//		self.clear();
//		self.currentIndex(currentIndex - self.pageSize());  
//		self.loadFunc(self.currentIndex(), self.getPageEndIndex()); 
		self.loadPage(self.currentPage() - 1);
		
	};
	self.loadNext = function(){ 
		self.loadPage(self.currentPage() + 1);
	};
	
	self.clear = function(){
		self.rows.remove(function(item) {
			return true;
		});
	};
	
	self.loadRows = function(){ 
		self.clear();
		self.currentPage(0); 
		self.loadFunc(self.currentIndex(), self.currentIndex() + self.pageSize()); 
	};  
	
	self.addRows = function(result){   
		$.each(result, function(i, n){
			self.rows.push(n);
		}); 
	};
	self.loadPageRows = function(totalCount, rows){
		self.addRows(rows);
		self.totalCount(totalCount);
		self.endIndex(self.currentIndex()%self.pageSize()==0 ? ((self.totalCount() == 0) ? 0 : self.currentIndex() + self.pageSize()) : self.currentIndex() + self.currentIndex()%self.pageSize());
		self.pageCount((self.totalCount() + (self.pageSize() - self.totalCount()% self.pageSize()))/self.pageSize());
	}; 
	
	self.loadPage = function(pageIndex){  
		if(pageIndex == self.currentPage()){
			return;
		}   
		self.clear();
		self.currentPage(pageIndex);
		self.currentIndex(pageIndex * self.pageSize());
		self.loadFunc(self.currentIndex() + 1, self.currentIndex() + self.pageSize());  
	};  
}