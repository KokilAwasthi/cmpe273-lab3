/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.sjsu.cmpe.cache.client;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import com.google.common.hash.*;
//import java.nio.charset.Charset;

/**
 *
 * @author Kokil
 */
public class ConsistentHash<T> {
 
  private final HashFunction hashFunction;
  private final int numberOfReplicas;
  private final SortedMap<Integer, T> circle =
    new TreeMap<Integer, T>();

  public ConsistentHash(HashFunction hashFunction,
    int numberOfReplicas, Collection<T> nodes) {
   
    this.hashFunction = hashFunction;
    this.numberOfReplicas = numberOfReplicas;

    for (T node : nodes) {
      add(node);
    }
  }

  public void add(T node) {
    for (int i = 0; i < numberOfReplicas; i++) {
     circle.put(hashFunction.hashUnencodedChars(node.toString() + i).asInt(), node);
       
    }
    
  }

  public void remove(T node) {
    for (int i = 0; i < numberOfReplicas; i++) {
      circle.remove(hashFunction.hashUnencodedChars(node.toString() + i).asInt());
    }
  }

  public T get(long key) {
    if (circle.isEmpty()) {
      return null;
    }
    int hash = hashFunction.hashLong(key).asInt();
   
    if (!circle.containsKey(hash)) {
      SortedMap<Integer, T> tailMap = circle.tailMap(hash);
      hash = tailMap.isEmpty()? circle.firstKey() : tailMap.firstKey();
    }
    return circle.get(hash);
 } 
 
    
}
