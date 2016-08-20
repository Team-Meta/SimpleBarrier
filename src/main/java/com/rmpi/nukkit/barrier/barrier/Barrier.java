package com.rmpi.nukkit.barrier.barrier;

import java.io.Serializable;

import cn.nukkit.level.Position;

class Barrier implements Serializable {
	private static final long serialVersionUID = 4696052540024990223L;
	
	public int x1, x2,
		y1, y2,
		z1, z2;
	public int levelId;
	public String name;
	
	public boolean isInside(Position pos) {
		return pos.getLevel().getId() == levelId &&
				pos.x >= Math.min(x1, x2) && pos.x <= Math.max(x1, x2) &&
				pos.y >= Math.min(y1, y2) && pos.y <= Math.max(y1, y2) &&
				pos.z >= Math.min(z1, z2) && pos.z <= Math.max(z1, z2);
	}
}
