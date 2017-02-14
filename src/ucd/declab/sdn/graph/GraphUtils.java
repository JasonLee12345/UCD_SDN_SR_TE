package ucd.declab.sdn.graph;

public class GraphUtils {

	static class SpritePair {
		double spritePos = 1;
		boolean signChange = false;

		public SpritePair(double spritePos, boolean signChange) {
			this.spritePos = spritePos;
			this.signChange = signChange;
		}

		public void incrementSpritePos() { this.spritePos++; }
		public void switchSignChange() { this.signChange = !this.signChange; }
		
		public void setSpritePos(double spritePos) { this.spritePos = spritePos; }
		public void setSignChange(boolean signChange) { this.signChange = signChange; }
		
		public double getSpritePos() { return spritePos; }
		public boolean getSignChange() { return signChange; }
	}
}
