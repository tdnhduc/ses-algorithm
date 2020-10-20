package com.hcmus.fit.network.distributedsystem.utils;

import java.util.List;

public class Buffer {
    private List<Tuple> bufferMessages;

    public Buffer(List<Tuple> bufferMessages){
        this.bufferMessages = bufferMessages;
    }

    public void insert(Tuple inComingBuffer) {
        boolean flag = true;
        for (Tuple buffer : this.bufferMessages) {
            if (buffer.getPid() == inComingBuffer.getPid()) {
                // overwrite timestamp
                buffer.setTimeStamp(inComingBuffer.getTimeStamp());
                flag = false;
                break;
            }
        }
        if (flag) {
            // if inComing is a new one, add in buffers
            this.bufferMessages.add(inComingBuffer);
        }
    }

    public boolean deliveryCondition(List<Tuple> bufferMessages, Tuple currProcess) {
        for (Tuple bufferMessage : bufferMessages) {
            if (bufferMessage.getPid() == currProcess.getPid()) {
                // TODO : compare vector clock
                return VectorClock.compareLessThan(bufferMessage.getTimeStamp(), currProcess.getTimeStamp());
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

    public static List<Tuple> merge(List<Tuple> bufferMessages, List<Tuple> inComingBufferMessages) {
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
}
