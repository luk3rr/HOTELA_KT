--liquibase formatted sql

--preconditions onFail:HALT onError:HALT

--changeset lucas.araujo:003-insert-room-types

SET search_path TO hotela;
------------------------------------------------------------------------------------------------------------------------

INSERT INTO room_type (id, name, description)
VALUES ('50d14717-f2bd-43ea-9f24-600b850734a6', 'Standard Solteiro', 'Quarto padrão com uma cama de solteiro'),
       ('09f600e7-88e6-4fd7-a735-fb054c0e0faa', 'Standard Casal', 'Quarto padrão com uma cama de casal'),
       ('b996b787-0fee-4852-ba4e-2ce3165d3408', 'Casal Deluxe',
        'Quarto com cama de casal, varanda e amenidades premium'),
       ('aedb645a-53a8-4c5d-82bd-8100327a5088', 'Twin Econômico',
        'Quarto com duas camas de solteiro e decoração simples'),
       ('7dbd55a7-a1ef-4bd9-a6e9-944ba7bc0720', 'Suíte Executiva',
        'Suíte com área de trabalho, sofá e serviço de quarto'),
       ('e9d57364-878e-426f-bfa0-ef052e9d01cf', 'Suíte Presidencial Vista Mar',
        'Suíte de luxo com vista panorâmica para o mar'),
       ('aa079180-ee6e-427c-81fd-b1cf4c0eb7ec', 'Quarto Família', 'Quarto espaçoso com múltiplas camas para famílias'),
       ('549c66e7-2802-4a83-9d1f-bb82458e07e2', 'Studio Luxo', 'Studio com cozinha compacta e cama king-size'),
       ('f2536a1e-3ad4-4de3-a231-fccd4b4dda41', 'Loft Temático', 'Quarto em estilo loft com decoração temática'),
       ('e872c8b6-30b0-4be7-9c4c-b45b7aec042c', 'Bangalô Jardim',
        'Bangalô privativo com vista para o jardim e área externa');

--rollback DELETE FROM room_type WHERE id IN (
--rollback                                    '50d14717-f2bd-43ea-9f24-600b850734a6',
--rollback                                    '09f600e7-88e6-4fd7-a735-fb054c0e0faa',
--rollback                                    'b996b787-0fee-4852-ba4e-2ce3165d3408',
--rollback                                    'aedb645a-53a8-4c5d-82bd-8100327a5088',
--rollback                                    '7dbd55a7-a1ef-4bd9-a6e9-944ba7bc0720',
--rollback                                    'e9d57364-878e-426f-bfa0-ef052e9d01cf',
--rollback                                    'aa079180-ee6e-427c-81fd-b1cf4c0eb7ec',
--rollback                                    '549c66e7-2802-4a83-9d1f-bb82458e07e2',
--rollback                                    'f2536a1e-3ad4-4de3-a231-fccd4b4dda41',
--rollback                                    'e872c8b6-30b0-4be7-9c4c-b45b7aec042c');
