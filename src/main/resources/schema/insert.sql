INSERT INTO customers (name, email) VALUES
    ('Anna Svensson', 'anna.svensson@example.com'),
    ('Erik Johansson', 'erik.johansson@example.com'),
    ('Maria Lindberg', 'maria.lindberg@example.com'),
    ('Johan Karlsson', 'johan.karlsson@example.com');

INSERT INTO bajamajas (name, rental_rate, number_of_stalls, handicap, type) VALUES
    ('Classic Standard', 250.00, 1, false, 'BAJAMAJA'),
    ('Classic Plus', 300.00, 1, true, 'BAJAMAJA'),
    ('Event Duo', 450.00, 2, false, 'BAJAMAJA'),
    ('Event Duo Handicap', 520.00, 2, true, 'BAJAMAJA'),
    ('Festival Pro 4', 850.00, 4, false, 'BAJAMAJA'),
    ('Festival Pro 4 Accessible', 920.00, 4, true, 'BAJAMAJA'),
    ('Construction Basic', 200.00, 1, false, 'BAJAMAJA'),
    ('Construction XL', 350.00, 2, false, 'BAJAMAJA'),
    ('Luxury Event Suite', 1200.00, 3, true, 'BAJAMAJA'),
    ('Mega Festival 8', 1600.00, 8, false, 'BAJAMAJA');

INSERT INTO decorations (name, rental_rate, color, type) VALUES
    ('Red Carpet Deluxe', 500.00, 'RED', 'DECORATION'),
    ('Blue Stage Curtains', 350.00, 'BLUE', 'DECORATION'),
    ('Golden VIP Rope', 200.00, 'GOLD', 'DECORATION'),
    ('Paper holder', 800.00, 'WHITE', 'DECORATION'),
    ('Black Light System', 650.00, 'BLACK', 'DECORATION'),
    ('Green Garden Lights', 300.00, 'GREEN', 'DECORATION'),
    ('Purple Lounge Sofa', 750.00, 'PURPLE', 'DECORATION'),
    ('Silver Disco Ball', 400.00, 'SILVER', 'DECORATION'),
    ('Pink Balloon Package', 150.00, 'PINK', 'DECORATION'),
    ('Orange Festival Banner', 180.00, 'ORANGE', 'DECORATION');

INSERT INTO platforms (name, rental_rate, type) VALUES
    ('Small Event Platform', 1500.00, 'PLATFORM'),
    ('Medium Stage Platform', 3000.00, 'PLATFORM'),
    ('Festival Main Stage', 8000.00, 'PLATFORM'),
    ('Construction Lift Deck', 2000.00, 'PLATFORM'),
    ('VIP Viewing Deck', 5000.00, 'PLATFORM'),
    ('Compact Garden Platform', 1200.00, 'PLATFORM');

INSERT INTO join_platforms_bajamajas (platform_id, bajamaja_id) VALUES
    (1, 1),
    (1, 2);

-- Medium Stage Platform (id 2)
INSERT INTO join_platforms_bajamajas (platform_id, bajamaja_id) VALUES
    (2, 3),
    (2, 4),
    (2, 5);

-- Festival Main Stage (id 3)
INSERT INTO join_platforms_bajamajas (platform_id, bajamaja_id) VALUES
    (3, 5),
    (3, 6),
    (3, 7),
    (3, 10);

-- Construction Lift Deck (id 4)
INSERT INTO join_platforms_bajamajas (platform_id, bajamaja_id) VALUES
    (4, 7),
    (4, 8);

-- VIP Viewing Deck (id 5)
INSERT INTO join_platforms_bajamajas (platform_id, bajamaja_id) VALUES
    (5, 2),
    (5, 6),
    (5, 9);

-- Compact Garden Platform (id 6)
INSERT INTO join_platforms_bajamajas (platform_id, bajamaja_id) VALUES
    (6, 1);

INSERT INTO rentals
(customer_id, rental_object_type, rental_object_id, start_date, end_date, return_date, dailyRate, originalPayment, extraPayment, refund)
VALUES

-- Pågående (ej returnerad)
(1, 'BAJAMAJA', 1,
 '2026-02-25 10:00:00',
 '2026-02-26 10:00:00',
 NULL,
 250.00,
 500.00,
 NULL,
 NULL),

-- Försenad 2 dagar
(2, 'BAJAMAJA', 2,
 '2026-02-01 09:00:00',
 '2026-02-03 09:00:00',
 '2026-02-05 10:30:00',
 300.00,
 600.00,
 720.00,
 0),

-- Avbokad 10 dagar innan start (full refund)
(1, 'PLATFORM', 1,
 '2026-03-20 08:00:00',
 '2026-03-22 08:00:00',
 '2026-03-10 12:00:00',
 1500.00,
 3000.00,
 0,
 3000.00),

-- Returnerad i tid
(3, 'DECORATION', 1,
 '2026-02-15 12:00:00',
 '2026-02-17 12:00:00',
 '2026-02-17 11:30:00',
 500.00,
 1000.00,
 0,
 0),

-- Framtida bokning
(2, 'BAJAMAJA', 3,
 '2026-03-05 09:00:00',
 '2026-03-08 09:00:00',
 NULL,
 450.00,
 1350.00,
 NULL,
 NULL);

