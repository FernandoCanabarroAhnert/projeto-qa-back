INSERT INTO colaborador (id, matricula, nome, salario_base, tipo_colaborador) VALUES
-- PADRAO
(1,  'MAT0001', 'Ana Beatriz Souza', 3200.00, 'PADRAO'),
-- COMISSIONADO
(2, 'MAT0002', 'Eduardo Monteiro', 2500.00, 'COMISSIONADO'),
-- PRODUCAO
(3, 'MAT0003', 'José Antônio Ferraz', 1800.00, 'PRODUCAO');

INSERT INTO colaborador_padrao (id) VALUES (1);
INSERT INTO colaborador_comissionado (id, porcentagem_comissao, valor_vendas) VALUES (2, 5.00,  45000.00);
INSERT INTO colaborador_producao (id, quantidade_produzida, valor_unidade_produzida) VALUES (3,  850, 3.50);
ALTER TABLE colaborador ALTER COLUMN id RESTART WITH 4;