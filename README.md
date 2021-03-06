# testpushandnativ

## Общее описание:
Cделать приложение для Android, которое выполнит хеширование данных по алгоритму SHA-256.
Данные необходимо получить из пуш-уведомления.
Хеширование должно выполняться в нативном коде (написано на С++)
Порядок работы приложения:
1. В приложение приходит пуш, в котором в поле «toHash» содержится строка произвольной длины (UTF-8)
2. Приложение обрабатывает пуш-уведомление, извлекает строку и считает от нее значение SHA-256. Вычисление выполняется в нативном коде (написанном на языке С++)
3. Приложение помещает вычисленный код в поле для ввода на главном экране, добавляя строки к  ранее внесенным записям. Например
`17:50:12, toHash: 123456, hash value: 8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92`
`17:50:13, toHash: 654321, hash value: 481f6cc0511143ccdd7e2d1b1b94faf0a700a8b49cd13922a70b5ae28acaa8c5`
4. Если пуш пришел, когда приложение открыто – вычисление производится сразу, результат добавляется в поле для ввода (см. п. 3)
5. Если пуш пришел, когда приложение закрыто (или находится в background), то `a.` должны быть проведены вычисления `b.` в Notification Bar должно быть выведено уведомление «Произведено N вычислений», где N – это количество пушей, пришедших, пока приложение закрыто или в фоне `c.` уведомление от приложения в Notification Bar должно быть в единственном экземпляре `d.` при переходе в приложение из уведомления или при открытии приложения `i)` результаты вычислений должны быть отображены в соответствии с п. 3 `ii)` уведомление (при наличии) должно быть убрано из Notification Bar.

# FCM

Request without notification section to send push https://reqbin.com/nryktlkg

Replace YOURTOKEN to your token form FCM.

`{
  "to": "YOURTOKEN",
  "data": {
    "to_calc": "123456"
  }
}`

Recomendate replace YOURAPIKEY
`Authorization: key=YOURAPIKEY`

# LIB
For calc used picosha2

# Screenshots

![image](https://github.com/mksmbrtsh/testpushandnativ/blob/main/screenshots/1.jpg)
![image](https://github.com/mksmbrtsh/testpushandnativ/blob/main/screenshots/2.jpg)
![image](https://github.com/mksmbrtsh/testpushandnativ/blob/main/screenshots/3.jpg)
![image](https://github.com/mksmbrtsh/testpushandnativ/blob/main/screenshots/4.jpg)
