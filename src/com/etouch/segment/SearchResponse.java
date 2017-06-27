package com.etouch.segment;

import java.util.List;

public class SearchResponse<T> {

	    private List<T> elements;
	    //public List<ContactField> elements;
	    private int total;
	    private int page;
	    private int pageSize;
	    private List<T> items;
	    private int totalResults;

	    
	    public List<T> getItems() {
			return items;
		}

		public void setItems(List<T> items) {
			this.items = items;
		}

		public int getTotalResults() {
			return totalResults;
		}

		public void setTotalResults(int totalResults) {
			this.totalResults = totalResults;
		}

		public List<T> getElements() {
	        return elements;
	    }

	    public void setElements(List<T> elements) {
	        this.elements = elements;
	    }

	    public int getPage() {
	        return page;
	    }

	    public void setPage(int page) {
	        this.page = page;
	    }

	    public int getPageSize() {
	        return pageSize;
	    }

	    public void setPageSize(int pageSize) {
	        this.pageSize = pageSize;
	    }

	    public int getTotal() {
	        return total;
	    }

	    public void setTotal(int total) {
	        this.total = total;
	    }
	}

