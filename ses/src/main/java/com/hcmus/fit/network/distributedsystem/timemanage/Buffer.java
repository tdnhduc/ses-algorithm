package com.hcmus.fit.network.distributedsystem.timemanage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Buffer {
    private List<Tuple> bufferMessages;
    private static final Logger LOGGER = LoggerFactory.getLogger(Buffer.class);

    public Buffer(List<Tuple> bufferMessages){
        this.bufferMessages = bufferMessages;
    }

    public synchronized void insert(Tuple inComingBuffer) {
        boolean flag = true;
        for (Tuple buffer : this.bufferMessages) {
            if (buffer.getPid() == inComingBuffer.getPid()) {
                // overwrite timestamp
                LOGGER.info("[OVERWRITE] over write timestamp message of process {}, with replace timestamp={}",buffer.getPid(), buffer.getTimestamp());
                //TimeStamp.setTimestamp(inComingBuffer.getTimeStampIncoming());
                flag = false;
                break;
            }
        }
        if (flag) {
            // if inComing is a new one, add in buffers
            this.bufferMessages.add(inComingBuffer);
            LOGGER.info("[NEW NODE] process {} send new message to me with timestamp={}", inComingBuffer.getPid(), inComingBuffer.getTimestamp());
        }
    }

    public synchronized static boolean deliveryCondition(List<Tuple> bufferMessages, Tuple currProcess) {
        for (Tuple bufferMessage : bufferMessages) {
            if (bufferMessage.getPid() == currProcess.getPid()) {
                return VectorClock.compareLessThan(bufferMessage.getTimestamp(), currProcess.getTimestamp());
            }
        }
        return true;
    }

    /**
     * @param bufferMessages : local buffer of process
     * @param inComingBuffer : incoming buffer from different process
     * @return index of inComingBuffer in local buffer, otherwise return -1
     */
    private static int getIndexInBufferMessage(List<Tuple> bufferMessages, Tuple inComingBuffer) {
        // if bufferMessage have been order, checkExist is faster
        // TODO : of course :grinning: :grinning:
        for (Tuple buffer : bufferMessages) {
            if (buffer.getPid() == inComingBuffer.getPid()) {
                return bufferMessages.indexOf(buffer);
            }
        }
        return -1;
    }

    public synchronized static List<Tuple> merge(List<Tuple> bufferMessages, List<Tuple> inComingBufferMessages) {
        // TODO: merge two list with pid incrementally. for fun :smile: :smile:
        for (Tuple buffer : inComingBufferMessages) {
            int indexBuffer = getIndexInBufferMessage(bufferMessages, buffer);
            if (indexBuffer != -1) {
                bufferMessages.set(indexBuffer, buffer);
            } else {
                bufferMessages.add(buffer);
            }
        }
        return bufferMessages;
    }

    public synchronized void insertLocalBuffer(List<Tuple> buffers){
        this.bufferMessages = buffers;
    }

    public List<Tuple> getBufferMessages() {
        return bufferMessages;
    }
}
