package nlprog.ex3;

// a receive object 
// stores the message received by another process
// and is used for synchronization
// ------------------------------------------------------------------------
class Receive {
	String message = null;
}

// ------------------------------------------------------------------------
class Process extends Thread {
	int m_pid; // process id
	Process[] m_processes; // references to all other processes
	Receive[] m_receive; // the receive objects

	Process(int pid, int n) {
		m_pid = pid;
		if (m_pid == 0) { //Only the first process has the array of references 
			m_processes = new Process[n];
			m_processes[0] = this;
		}
		m_receive = new Receive[n];
		for (int i = 0; i < n; ++i) {
			m_receive[i] = new Receive();
		}
	}

	// remote, called by p0, at all others, to transfer the reference vector
	public synchronized void neighbours(Process[] p) {
		m_processes = p;
		notify();
	}

	// remote, called by others, at p0, to send their reference to p0
	public synchronized void register(Process p, int pid) {
		assert(pid != 0);
		m_processes[pid] = p; // p0 stores references in the vector
		notify();
		System.out.println("p" + pid + " registered");
	}

	// called by sender
	public synchronized void sendMessage(String m, int pid) {
		m_receive[pid].message = m;
		notify();
	}

	// called by a process to receive a message from another process
	private synchronized String receiveMessage(int i) {
		if(m_receive[i].message == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return m_receive[i].message;
	}

	// the activity of a process
	public void run() {
		if (m_pid == 0) {
			synchronized (this) {
				for(int i = 1; i < m_processes.length; ++i) {
					if(m_processes[i] == null) {
						try {
							System.out.println("Waiting on process " + i);
							wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}

			// p0 sends the reference vector to the others
			for (int i = 1; i < m_processes.length; ++i) {
				try {
					m_processes[i].neighbours(m_processes);
				} catch (Exception e) {
					System.err.println("init exception:");
					e.printStackTrace();
				}
			}
		} else {
			// wait until they got the reference vector
			synchronized (this) {
				try {
					while(m_processes == null) {
						wait();
					}
				} catch (InterruptedException e) {
				}
			}
		}

		// send hello to every other process
		for (int i = 0; i < m_processes.length; ++i) {
			if (i != m_pid) {
				try {
					m_processes[i].sendMessage("hello p" + i + ", this is p" + m_pid, m_pid);
				} catch (Exception e) {
					System.err.println("send exception:");
				}
				System.out.println(m_pid + " sent hello to p" + i);
			}
		}

		// receive message from every other one
		for (int i = m_processes.length - 1; i >= 0; --i) {
			if (i != m_pid) {
				System.out.println(m_pid + " receiving from p" + i + " ...");
				String m = receiveMessage(i);
				System.out.println(m_pid + " received  from p" + i + ": " + m);
			}
		}

	}

	public static void main(String[] args) {
		int n = 3;
		if(args.length > 0) {
			n = Integer.parseInt(args[0]);
		}

		System.out.println("number of processes: " + n);

		Process one = new Process(0, n);
		one.start();

		for (int i = 1; i < n; i++) {
			Process temp = new Process(i, n);
			one.register(temp, i);
			temp.start();
		}

	}

}
