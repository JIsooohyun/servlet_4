package com.sh.board.qna;

import com.sh.board.BoardDTO;

public class QnaDTO extends BoardDTO{
	private int ref;
	private int depth;
	private int step;
	
	public int getRef() {
		return ref;
	}
	public void setRef(int ref) {
		this.ref = ref;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
}
