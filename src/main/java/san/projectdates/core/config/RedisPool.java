package san.projectdates.core.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {
  private static JedisPool pool;

  static{
    JedisPoolConfig poolConfig = new JedisPoolConfig();

    poolConfig.setMaxTotal(10);
    poolConfig.setMaxIdle(5);
    poolConfig.setMinIdle(1);
  
    poolConfig.setTestOnBorrow(true);
    poolConfig.setTestOnReturn(true);

    pool = new JedisPool(poolConfig, "localhost", 6379);
  }

  public static Jedis getResource(){
    return pool.getResource();
  }

  public static void close(){
    if(pool != null){
      pool.close();
    }
  }
}
