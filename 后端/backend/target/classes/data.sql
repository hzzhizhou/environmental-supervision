USE epm_db;

INSERT IGNORE INTO roles (role_name, role_code) VALUES ('系统管理员', 'ADMIN');
INSERT IGNORE INTO roles (role_name, role_code) VALUES ('网格员', 'WORKER');
INSERT IGNORE INTO roles (role_name, role_code) VALUES ('公众监督员', 'PUBLIC');
INSERT IGNORE INTO roles (role_name, role_code) VALUES ('决策者', 'DECIDER');

INSERT IGNORE INTO grids (grid_code, grid_name, province_id, city_id, district, address, status) VALUES ('LN-SY-HP-001', '和平区网格001', 6, 92, '和平区', '和平区文化路XX号', 1);
INSERT IGNORE INTO grids (grid_code, grid_name, province_id, city_id, district, address, status) VALUES ('LN-SY-HP-002', '和平区网格002', 6, 92, '和平区', '和平区南京街XX号', 1);
INSERT IGNORE INTO grids (grid_code, grid_name, province_id, city_id, district, address, status) VALUES ('LN-SY-SH-001', '沈河区网格001', 6, 92, '沈河区', '沈河区中街路XX号', 1);
INSERT IGNORE INTO grids (grid_code, grid_name, province_id, city_id, district, address, status) VALUES ('LN-SY-SH-002', '沈河区网格002', 6, 92, '沈河区', '沈河区五爱街XX号', 1);
INSERT IGNORE INTO grids (grid_code, grid_name, province_id, city_id, district, address, status) VALUES ('LN-SY-HG-001', '皇姑区网格001', 6, 92, '皇姑区', '皇姑区长江街XX号', 1);
