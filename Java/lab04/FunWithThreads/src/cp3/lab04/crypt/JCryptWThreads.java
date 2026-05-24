package cp3.lab04.crypt;

import java.util.concurrent.atomic.AtomicInteger;

public class JCryptWThreads extends Thread {
    JCryptUtil.Options opts;
    int index;
    AtomicInteger index2;

    public JCryptWThreads(JCryptUtil.Options opts, int index) {
        this.opts = opts;
        this.index = index;
    }

    public JCryptWThreads(JCryptUtil.Options opts, AtomicInteger index2) {
        this.opts = opts;
        this.index2 = index2;
    }

    @Override
    public void run() {
        if (index2 == null) {
            try {
                JCrypt.process(opts, index);
            }
            catch (JCryptUtil.Problem e) {
                System.err.println(e.getMessage());
            }
        }
        else {
            int i;
            while((i = index2.getAndIncrement()) < (opts.filenames.length)) {
                try {
                    JCrypt.process(opts, i);
                }
                catch (JCryptUtil.Problem e) {
                    System.err.println(e.getMessage());
                }
            }
        }

    }
}
