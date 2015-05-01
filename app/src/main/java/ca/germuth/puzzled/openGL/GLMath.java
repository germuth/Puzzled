package ca.germuth.puzzled.openGL;

public class GLMath {
	public static final char X_AXIS = 'X';
	public static final char Y_AXIS = 'Y';
	public static final char Z_AXIS = 'Z';
	public static final char ALL_AXIS = 'A';
	
	public static float[][] multiply(float[][] A, float[][] B) {

        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;

        if (aColumns != bRows) {
            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
        }

        float[][] C = new float[aRows][bColumns];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bColumns; j++) {
                C[i][j] = 0f;
            }
        }

        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < bColumns; j++) { // bColumn
                for (int k = 0; k < aColumns; k++) { // aColumn
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }

        return C;
	}
	
	private static float[][] rotationMatrix(char axis, float theta){
		if(axis == X_AXIS){
			return new float[][]{
					{1, 0, 0, 0},
					{0, cos(theta), sin(theta), 0},
					{0, -sin(theta), cos(theta), 0},
					{0, 0, 0, 1}
			};
		}else if(axis == Y_AXIS){
			return new float[][]{
					{cos(theta), 0, -sin(theta), 0},
					{0, 1, 0, 0},
					{sin(theta), 0, cos(theta), 0},
					{0, 0, 0, 1}
			};
		}else if(axis == Z_AXIS){
			return new float[][]{
					{cos(theta), sin(theta), 0, 0},
					{-sin(theta), cos(theta), 0, 0},
					{0, 0, 1, 0},
					{0, 0, 0, 1}
			};
		}else if(axis == ALL_AXIS){
			return multiply( multiply(rotationMatrix(X_AXIS, theta), rotationMatrix(Y_AXIS, theta) ),
					rotationMatrix(Z_AXIS, theta));
		}
		return null;
	}
	
	public static float sin(float angle){
		return (float) Math.sin(angle);
	}
	
	public static float cos(float angle){
		return (float) Math.cos(angle);
	}
}
