package upe.process;


public interface UProcessImageField extends UProcessField {
	public void setImage( byte[] imgData );
	public byte[] getImage();
}
