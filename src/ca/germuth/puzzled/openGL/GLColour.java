package ca.germuth.puzzled.openGL;

/**
 * Represents a colour in the openGL 3d model
 * @author Administrator
 *
 */
public class GLColour {

	private final float red;
	private final float green;
	private final float blue;
	private final float alpha;

	public GLColour(float red, float green, float blue){
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = 0x10000;
	}

	public GLColour(float red, float green, float blue, float alpha){
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	/**
	 * Testes whether two colours are equal by
	 * their red, green, blue and alpha values
	 */
	@Override
	public boolean equals(Object o) {
		if( o instanceof GLColour ){
			GLColour g = (GLColour) o;
			if( this.red == g.red &&
					this.green == g.green &&
					this.blue == g.blue &&
					this.alpha == g.alpha ){
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the red
	 */
	public float getRed() {
		return red;
	}

	/**
	 * @return the green
	 */
	public float getGreen() {
		return green;
	}

	/**
	 * @return the blue
	 */
	public float getBlue() {
		return blue;
	}

	/**
	 * @return the alpha
	 */
	public float getAlpha() {
		return alpha;
	}
}
