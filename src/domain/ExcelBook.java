package domain;

public class ExcelBook {
	private String title;
	private String author;
	private String company;
	private String isbn;
	private String imgUrl;

	public ExcelBook() {}
	
	public ExcelBook(String title, String author, String company) {
		this.title = title;
		this.author = author;
		this.company = company;
	}

	public ExcelBook(String title, String author, String company, String isbn, String imgUrl) {
		this.title = title;
		this.author = author;
		this.company = company;
		this.isbn = isbn;
		this.imgUrl = imgUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	@Override
	public String toString() {
		return "ExcelBook [title=" + title + ", author=" + author + ", company=" + company + ", isbn=" + isbn
				+ ", imgUrl=" + imgUrl + "]";
	}
}
