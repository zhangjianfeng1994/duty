package com.boco.eoms.commons;



public class PageModel {
	
    //查询记录数  
    private int totalRecords;  
    //每页多少条数据  
    private int pageSize;  
    //第几页  
    private int pageNo;  
    
    //开始记录条数
    public String begainPageNum;
    //结束记录条数
    public String endPageNum;
    //查询当前页
    public int currnetPageNum;
    
    
    public int getCurrnetPageNum() {
    	if(currnetPageNum==0){
    		return currnetPageNum+1;
    	}else{
    		return currnetPageNum;
    	}
		
	}

	public void setCurrnetPageNum(int currnetPageNum) {
		this.currnetPageNum = currnetPageNum;
	}

	public String getBegainPageNum() {
		return begainPageNum;
	}

	public void setBegainPageNum(String begainPageNum) {
		this.begainPageNum = begainPageNum;
	}

	public String getEndPageNum() {
		return endPageNum;
	}

	public void setEndPageNum(String endPageNum) {
		this.endPageNum = endPageNum;
	}

	public PageModel(int totalRecords,int pageSize,int pageNo){
    	this.begainPageNum=String.valueOf((pageNo-1)*pageSize+1);
    	this.endPageNum=String.valueOf((pageNo)*pageSize);
    	this.pageSize=pageSize;
    	this.totalRecords=totalRecords;
    	this.currnetPageNum=pageNo;
    	
    }
    
    /** 
     * 总页数 
     * @return 
     */  
    public int getTotalPages(){ 
    	if(totalRecords>pageSize){
    		if((totalRecords)%pageSize==0){
    			return (totalRecords)/pageSize;  
    		}else{
    			return (totalRecords)/pageSize+1;  
    		}
    		
    	}else{
    		return 1;
    	}
        
    }  
    /** 
     * 取得首页 
     * @return 
     */  
    public int getTopPageNo(){  
        return 1;  
    }  

    /** 
     * 上一页 
     * @return 
     */  
    public int getPreviousPageNo(){  
        if(pageNo<=1){  
            return 1;  
        }  
        return pageNo-1;  
    }  
    /** 
     * 下一页 
     * @return 
     */  
    public int getNextPageNo(){  
        if(pageNo>=getBottomPageNo()){  
            return getBottomPageNo();  
        }  
        return pageNo+1;  
    }  
    /** 
     * 取得尾页 
     * @return 
     */  
    public int getBottomPageNo(){  
        return getTotalPages();  
    }  

    public int getTotalRecords() {  
        return totalRecords;  
    }  
    public void setTotalRecords(int totalRecords) {  
        this.totalRecords = totalRecords;  
    }  
    public int getPageSize() {  
        return pageSize;  
    }  
    public void setPageSize(int pageSize) {  
        this.pageSize = pageSize;  
    }  
    public int getPageNo() {  
        return pageNo;  
    }  
    public void setPageNo(int pageNo) {  
        this.pageNo = pageNo;  
    }  
}