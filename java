HashMap基础
1.8和1.7中的Node的int hash field
find bucket index 为什么不用取余？
容易取到重复的bucketIndex
为什么array的长度是2的整数幂？
和java中如何实现取到bucketIndex有关
在方法是：hash & （array.length-1）
什么时候扩容： 在size（） >=  threshold = length * factor
resize():
如果 array== null){
      initialize array;
} else
扩容： oldCap << 1(即*2）（注, 如果原oldLength已经到了上限, 则newLength = oldLength);）
rehash: 1.8 中对于1.7有优化
1.7 直接是重新算每个entry的index，
1.8 如下计算：because we are using power-of-two expansion, the elements from each bin must either stay at same index, or move with a power of two offset in the new table. The power of two offset 
e.hash & (newCap - 1)




HashMap Implementation in Java


Functions (for users):
fields：
get(by Key) return value
put(key, value) return key/value// resize-rehashing
remove(key), return true/false
size(), return int 
containsKey(key) return true/false
isEmpty() return true/false

Data structure
     array of Entries 
     the entry is actually a singly linked list(handle hash collision)
     helper functions:
           // use hash() function to get the hash value of the key;
          // then mod the length of the array to get the index.
          // get the head of the linked list and go through the list.
     private int hash();
     private int getindex(K key);
     private boolean equalsKey(K key1, K key2);
     // use it in update
    private boolean equalsKey(V value1, V v2);

sychronized() 缩小critical section 比如put这个方法中getIndex()一样的 

     
class MyHashMap<K,V>{
    private Node<K, V>[] array;
    private static float  DEFAULT_LOAD_FACTER = 0.75f;// hash collision
    private static int DEFAULT_CAPACITY = 16; 2^n;
    private int size;
    private float loadFactor;
    public static class Node<K, V> {
        final K key;
        V value;
        node<K, V> next;
        Node (K key, V Value){
            this.key = key;
            this.value = value;
        }
       // getter & setter

    }
   // constructor
    public MyHashMap(){
          this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
         
    }
   public MyHashMap(int cap, float loadFactor){
         if(cap < 0){
             throw new IllegalArgumentException(“cap cannot be <=  0”);
         }
        this.array = (Node<K,V>[])(new Node[cap]);
        this.size = 0;
        this.loadFactor = loadFactor;

   }
   public int size(){
       return size;
   }
   public boolean isEmpty(){
       return size == 0;
   }
   private int hash(K key){
        if(key == null){
            return 0;
        }
        int code = key.hashCode() & 0x7FFFFFFF;// max_value in java (16进制）
        return code;
   }
   private int getIndex(K key){
       return hash(key) % array.length;
   }
   private boolean equalsKey(K k1, K k2){
       return k1 == k2 || k1 != null && k1.equals(k2);
      
   }
 
   private boolean equalsValue(V v1, V v2){
       return V1 == V2 || V1 != null && V2.equals(V2);
  }
  public boolean containsKey(K key){
       int index = getIndex(key);
       Node<K,V> node = array[index];
       while(node != null){
           if(equalsKey(key, node.key)){
                return true;
           }
           node = node.next;
       }
       return false;
  }
  public boolean containsValue(V value){
       if(isEmpty()){
            return false;
       }
       for(Node<K,V> node: array){
           if(equalsValue(value, node.value)){
                 return true;
           }
       }
       return false;
  }
// rehash(）
// helper functions: 
private boolean needRehashing(){
      float factor = (size + 0.0f) / array.length;
      return factor>= DEFUALT_LOAD_FACTOR;
}
private void rehashing(){
   // resize
   int oldCap = array.length;
   Node<K, V>[] oldArray = array;
   array = (Node<K,V>[])(new Node[oldCap << 1]; 
   for(Node<K,V> node:oldArray){
         while(node != null){
              int index = getIndex(node.key);
              // insert at the head of the linked list;
              Node nxt = node.next;
              node.next = array[index];
              array[index] = node;
              node = nxt;
         }
   }
}
public V remove(K key){
    // delete a node in the linked list
    int index = getIndex(key);
    Node<K,V> node = array[index];
    Node<K,V> prev = null;
    while(node != null){
        if(equalsKey(node.key,key)){
            if(prev != null){
                  prev = node.next;
             }else{
                 array[index] = node.next;
            }
            node.next = null;
            size--;
            return node.value;

        }
        prev = node;
        node = node.next;
         
    }
    return null;
}
public V put(K key, V value){
     int index = getIndex(key);
     Node<K,V> head = array[index];
     Node<K,V> node = head;
     while(head != null){
         if(equalsKey(key, node.key)){
             // update
             V result = node.value;
             node.value = value;
             return value;
             
         }
         node = node.next;
     }
    Node<K,V> newNode = new Node<K, V>(key, value);
    newNode.next = head;
    array[index] = newNode;
     size++;
     if(needRehashing()){
         rehashing();
     }
     return null;
}

   


}
