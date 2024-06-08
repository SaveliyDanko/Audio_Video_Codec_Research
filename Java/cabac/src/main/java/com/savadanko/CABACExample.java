package com.savadanko;

import java.util.*;

public class CABACExample {

    // Симулированное контекстное состояние
    static class ContextModel {
        int state;
        int mps; // Most Probable Symbol (MPS)

        public ContextModel() {
            this.state = 0;
            this.mps = 0;
        }

        public int getState() {
            return state;
        }

        public int getMPS() {
            return mps;
        }

        public void update(int bin) {
            if (bin == mps) {
                // Transition to next state if MPS
                state = Math.min(state + 1, 63);
            } else {
                // Switch MPS and transition to next state if not MPS
                mps = 1 - mps;
                state = Math.max(state - 1, 0);
            }
        }
    }

    // Пример простого CABAC кодировщика
    static class CABACEncoder {
        List<Integer> encodedData;
        ContextModel[] contextModels;

        public CABACEncoder() {
            encodedData = new ArrayList<>();
            contextModels = new ContextModel[64];
            for (int i = 0; i < 64; i++) {
                contextModels[i] = new ContextModel();
            }
        }

        public void encodeBin(int bin, int ctxIdx) {
            ContextModel ctx = contextModels[ctxIdx];
            int mps = ctx.getMPS();

            // Сохранение закодированного значения (для простоты)
            if (bin == mps) {
                encodedData.add(0);
            } else {
                encodedData.add(1);
            }

            // Обновление контекста
            ctx.update(bin);
        }

        public List<Integer> getEncodedData() {
            return encodedData;
        }
    }

    // Пример простого CABAC декодера
    static class CABACDecoder {
        List<Integer> encodedData;
        ContextModel[] contextModels;
        int dataIndex;

        public CABACDecoder(List<Integer> encodedData) {
            this.encodedData = encodedData;
            contextModels = new ContextModel[64];
            for (int i = 0; i < 64; i++) {
                contextModels[i] = new ContextModel();
            }
            dataIndex = 0;
        }

        public int decodeBin(int ctxIdx) {
            ContextModel ctx = contextModels[ctxIdx];
            int mps = ctx.getMPS();

            // Чтение закодированного значения (для простоты)
            int bin = encodedData.get(dataIndex);
            dataIndex++;

            // Обновление контекста
            ctx.update(bin);

            return bin == 0 ? mps : 1 - mps;
        }
    }

    public static void main(String[] args) {
        // Пример данных для кодирования
        int[] data = {0, 1, 1, 0, 1, 0, 0, 1, 1};
        int ctxIdx = 0; // Для простоты используем один контекст

        // Кодирование данных
        CABACEncoder encoder = new CABACEncoder();
        for (int bin : data) {
            encoder.encodeBin(bin, ctxIdx);
        }
        List<Integer> encodedData = encoder.getEncodedData();

        // Декодирование данных
        CABACDecoder decoder = new CABACDecoder(encodedData);
        int[] decodedData = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            decodedData[i] = decoder.decodeBin(ctxIdx);
        }

        // Вывод результатов
        System.out.println("Original data: " + Arrays.toString(data));
        System.out.println("Encoded data: " + encodedData);
        System.out.println("Decoded data: " + Arrays.toString(decodedData));
    }
}
