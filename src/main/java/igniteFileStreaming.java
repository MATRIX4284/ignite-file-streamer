import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.affinity.AffinityUuid;
import org.apache.ignite.configuration.CacheConfiguration;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * Stream words into Ignite cache.
 * To start the example, you should:
 * <ul>
 *     <li>Start a few nodes using {@link ExampleNodeStartup}.</li>
 *     <li>Start streaming using {@link StreamWords}.</li>
 *     <li>Start querying popular words using {@link QueryWords}.</li>
 * </ul>
 */
public class igniteFileStreaming {
    /**
     * Starts words streaming.
     *
     * @param args Command line arguments (none required).
     * @throws Exception If failed.
     */

    private static final String CACHE_NAME = "liblu1";

    public static void main(String[] args) throws Exception {
        // Mark this cluster member as client.
        Ignition.setClientMode(true);

        try (Ignite ignite = Ignition.start("config/example-ignite.xml")) {
            //if (!ExamplesUtils.hasServerNodes(ignite))
            //return;

            // The cache is configured with sliding window holding 1 second of the streaming data.
            //IgniteCache<AffinityUuid, String> stmCache = ignite.getOrCreateCache(CacheConfig.wordCache());

            CacheConfiguration<AffinityUuid, String> cfg = new CacheConfiguration<>(CACHE_NAME);

            // Index key and value.
            cfg.setIndexedTypes(AffinityUuid.class, String.class);

            // Auto-close cache at the end of the example.
            try (IgniteCache<AffinityUuid, String> stmCache = ignite.getOrCreateCache(cfg)) {


                try (IgniteDataStreamer<AffinityUuid, String> stmr = ignite.dataStreamer(stmCache.getName())) {
                    // Stream words from "alice-in-wonderland" book.
                    while (true) {
                        InputStream in = igniteFileStreaming.class.getResourceAsStream("alice-in-wonderland.txt");

                        try (LineNumberReader rdr = new LineNumberReader(new InputStreamReader(in))) {
                            for (String line = rdr.readLine(); line != null; line = rdr.readLine()) {
                                for (String word : line.split(" "))
                                    if (!word.isEmpty())
                                        // Stream words into Ignite.
                                        // By using AffinityUuid we ensure that identical
                                        // words are processed on the same cluster node.
                                        stmr.addData(new AffinityUuid(word), word);
                                        //System.out.println(word);
                            }
                        }
                    }
                }
            }
        }
    }
}