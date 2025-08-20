#!/usr/bin/env node

/**
 * Тестовый скрипт для WebSocket API
 * 
 * Использование:
 * node test-websocket.js <JWT_TOKEN> <CHAT_ID>
 * 
 * Пример:
 * node test-websocket.js "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." "123e4567-e89b-12d3-a456-426614174000"
 */

const WebSocket = require('ws');

// Проверка аргументов командной строки
if (process.argv.length < 4) {
    console.log('Использование: node test-websocket.js <JWT_TOKEN> <CHAT_ID>');
    console.log('Пример: node test-websocket.js "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." "123e4567-e89b-12d3-a456-426614174000"');
    process.exit(1);
}

const jwtToken = process.argv[2];
const chatId = process.argv[3];

console.log('🔌 Подключение к WebSocket API...');
console.log(`📋 Chat ID: ${chatId}`);
console.log(`🔑 JWT Token: ${jwtToken.substring(0, 20)}...`);

// Создание WebSocket соединения
const ws = new WebSocket('ws://localhost:8080/ws');

ws.on('open', function open() {
    console.log('✅ WebSocket соединение установлено');
    
    // STOMP CONNECT frame
    const connectFrame = {
        command: 'CONNECT',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'accept-version': '1.2',
            'heart-beat': '10000,10000'
        },
        body: ''
    };
    
    console.log('📤 Отправка STOMP CONNECT...');
    ws.send(JSON.stringify(connectFrame));
});

ws.on('message', function message(data) {
    console.log('📨 Получено сообщение:', data.toString());
    
    try {
        const frame = JSON.parse(data.toString());
        
        if (frame.command === 'CONNECTED') {
            console.log('✅ STOMP соединение установлено');
            
            // Подписка на сообщения чата
            const subscribeFrame = {
                command: 'SUBSCRIBE',
                headers: {
                    'id': 'sub-0',
                    'destination': `/topic/chat/${chatId}`
                },
                body: ''
            };
            
            console.log(`📡 Подписка на топик: /topic/chat/${chatId}`);
            ws.send(JSON.stringify(subscribeFrame));
            
            // Отправка тестового сообщения
            setTimeout(() => {
                const sendFrame = {
                    command: 'SEND',
                    headers: {
                        'destination': `/app/chats/${chatId}/send`,
                        'content-type': 'application/json'
                    },
                    body: JSON.stringify({
                        text: `Тестовое сообщение от ${new Date().toISOString()}`
                    })
                };
                
                console.log('📤 Отправка тестового сообщения...');
                ws.send(JSON.stringify(sendFrame));
            }, 1000);
            
        } else if (frame.command === 'MESSAGE') {
            console.log('💬 Получено сообщение в чате:', frame.body);
            
            // Отправка еще одного сообщения через 2 секунды
            setTimeout(() => {
                const sendFrame = {
                    command: 'SEND',
                    headers: {
                        'destination': `/app/chats/${chatId}/send`,
                        'content-type': 'application/json'
                    },
                    body: JSON.stringify({
                        text: `Ответное сообщение от ${new Date().toISOString()}`
                    })
                };
                
                console.log('📤 Отправка ответного сообщения...');
                ws.send(JSON.stringify(sendFrame));
            }, 2000);
            
        } else if (frame.command === 'ERROR') {
            console.error('❌ STOMP ошибка:', frame.body);
        }
        
    } catch (error) {
        console.log('📨 Получено не-JSON сообщение:', data.toString());
    }
});

ws.on('error', function error(err) {
    console.error('❌ WebSocket ошибка:', err.message);
});

ws.on('close', function close() {
    console.log('🔌 WebSocket соединение закрыто');
});

// Обработка завершения процесса
process.on('SIGINT', function() {
    console.log('\n🛑 Завершение работы...');
    ws.close();
    process.exit(0);
});

// Автоматическое завершение через 10 секунд
setTimeout(() => {
    console.log('⏰ Автоматическое завершение через 10 секунд...');
    ws.close();
    process.exit(0);
}, 10000);
