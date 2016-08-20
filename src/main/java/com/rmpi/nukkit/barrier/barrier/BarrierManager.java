package com.rmpi.nukkit.barrier.barrier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rmpi.nukkit.barrier.SimpleBarrier;

import cn.nukkit.level.Position;

public class BarrierManager {
	private static BarrierManager instance = null;
	
	private List<Barrier> barriers = new ArrayList<>();
	
	private BarrierManager() {};
	
	public static BarrierManager getInstance() {
		return instance == null ? (instance = new BarrierManager()) : instance;
	}
	
	public void addBarrier(Position first, Position second, String name) {
		Barrier newBarrier = new Barrier();
		newBarrier.x1 = (int) first.x; newBarrier.x2 = (int) second.x;
		newBarrier.y1 = (int) first.y; newBarrier.y2 = (int) second.y;
		newBarrier.z1 = (int) first.z; newBarrier.z2 = (int) second.z;
		newBarrier.levelId = first.getLevel().getId();
		newBarrier.name = name;
		barriers.add(newBarrier);
	}
	
	public boolean deleteBarrier(String name) {
		return barriers.removeIf(barrier -> barrier.name.equals(name));
	}
	
	public void clear() {
		barriers.clear();
	}
	
	public boolean isInside(Position pos) {
		return barriers.stream().anyMatch(barrier -> barrier.isInside(pos));
	}
	
	public void init() {
		File dataFolder = new File(SimpleBarrier.getInstance().getDataFolder(), "barriers");
		
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
			return;
		}
		
		Arrays.stream(dataFolder.listFiles()).forEach(file -> {
			if (file.isDirectory()) return;
			
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
				barriers.add((Barrier) ois.readObject());
				ois.close();
			} catch (Exception e) {
				// TBD English locale
				SimpleBarrier.getInstance().getLogger().error("���� IO �� ����!\n�ܺ� ���μ����� ������ ������̰ų� �б� ������ �źεǾ��� �� �ֽ��ϴ�.\n �Ǵ� �踮�� ���˿� ���� �ʴ� ������ ������ �� ������ �ֽ��ϴ�.\n���ϸ�: " + file.getName());
			}
		});
	}
	
	public void save() {
		File dataFolder = new File(SimpleBarrier.getInstance().getDataFolder(), "barriers");
		if (!dataFolder.exists()) dataFolder.mkdirs();
		Arrays.stream(dataFolder.listFiles()).forEach(file -> {
			if (file.isDirectory()) return;
			for (Barrier barrier : barriers) if (barrier.name.equals(file.getName())) return;
			file.delete();
		});
		barriers.forEach(barrier -> {
			File targetFile = new File(dataFolder, barrier.name);
			
			try {
				if (!targetFile.exists()) targetFile.createNewFile();
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(targetFile));
				oos.writeObject(barrier);
				oos.close();
			} catch (Exception e) {
				// TBD English locale
				SimpleBarrier.getInstance().getLogger().error("���� IO �� ����!\n�ܺ� ���μ����� ������ ������̰ų� ���� ������ �źεǾ��� �� �ֽ��ϴ�.\n �Ǵ� �踮�� �̸��� �������Ҽ� �ֽ��ϴ�.\n���ϸ�: " + targetFile.getName());
			}
		});
	}
}
