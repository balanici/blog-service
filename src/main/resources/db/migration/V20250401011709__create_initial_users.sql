INSERT INTO public.authorities
(username, authority)
VALUES
('admin', 'write'),
('admin', 'read'),
('user', 'read');

INSERT INTO public.users
(username, password, enabled)
VALUES
('admin', 'admin', '1'),
('user', 'user', '1');
