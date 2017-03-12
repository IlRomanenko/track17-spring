package track.lessons.lesson3;

import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 * Двусвязный список
 */
public class MyLinkedList extends List {

    /**
     * private - используется для сокрытия этого класса от других.
     * Класс доступен только изнутри того, где он объявлен
     * <p>
     * static - позволяет использовать Node без создания экземпляра внешнего класса
     */
    private static class Node {
        Node prev;
        Node next;
        int val;

        Node(Node prev, Node next, int val) {
            this.prev = prev;
            this.next = next;
            this.val = val;
        }
    }

    private Node head = null;
    private Node tail = null;

    public MyLinkedList() {
    }

    private Node getNode(int idx) {
        Node current;
        if (idx < currentSize / 2) {
            current = head;
            while (idx > 0) {
                current = current.next;
                idx -= 1;
            }
        } else {
            idx = currentSize - idx - 1;
            current = tail;
            while (idx > 0) {
                current = current.prev;
                idx -= 1;
            }
        }
        return current;
    }

    @Override
    public void add(int item) {
        currentSize += 1;
        if (head == null) {
            head = new Node(null, null, item);
            tail = head;
        } else {
            Node newTail = new Node(tail, null, item);
            tail.next = newTail;
            tail = newTail;
        }
    }

    @Override
    public int remove(int idx) throws NoSuchElementException {
        checkIdx(idx);
        Node idxNode = getNode(idx);
        if (idxNode.prev != null) {
            idxNode.prev.next = idxNode.next;
        }
        if (idxNode.next != null) {
            idxNode.next.prev = idxNode.prev;
        }
        if (idx == 0) {
            head = head.next;
        }
        if (idx == currentSize - 1) {
            tail = tail.prev;
        }
        currentSize -= 1;
        return idxNode.val;
    }

    @Override
    public int get(int idx) throws NoSuchElementException {
        checkIdx(idx);
        return getNode(idx).val;
    }

}
