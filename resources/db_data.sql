insert into category(name) values
('Садовые'),
('Полевые');

insert into product(name, description, price, category_id, photo_url) values
('Букет Роз', 'Роза красная 40 см. - 9 шт.', 6000, 1, 'telegram-bot/src/main/resources/static/rose.png'),
('Букет Альстромерии', 'Альстромерии - 7 шт.', 6900, 1, 'telegram-bot/src/main/resources/static/alstromeria.png'),
('Букет Ромашки', 'Танацетум - 3 шт., ромашки - 7 шт.', 8200, 2, 'telegram-bot/src/main/resources/static/chamomile.png');
