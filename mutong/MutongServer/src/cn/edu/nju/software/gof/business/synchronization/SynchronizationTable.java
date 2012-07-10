package cn.edu.nju.software.gof.business.synchronization;

import java.util.LinkedList;
import java.util.List;

public class SynchronizationTable {

	private static List<Synchronizationable> synchronizations = new LinkedList<Synchronizationable>();

	static {
		synchronizations.add(new OAuthSynchronization());
		synchronizations.add(new RenRenSynchronization());
	}

	private SynchronizationTable() {

	}

	public static List<Synchronizationable> getSynchronizationList() {
		return synchronizations;
	}
}
