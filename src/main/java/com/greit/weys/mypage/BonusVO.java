package com.greit.weys.mypage;

public class BonusVO {

	private String barcode;
	private String barcodeUrl;
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getBarcodeUrl() {
		return barcodeUrl;
	}
	public void setBarcodeUrl(String barcodeUrl) {
		this.barcodeUrl = barcodeUrl;
	}
	@Override
	public String toString() {
		return "BonusVO [barcode=" + barcode + ", barcodeUrl=" + barcodeUrl + "]";
	}
}
