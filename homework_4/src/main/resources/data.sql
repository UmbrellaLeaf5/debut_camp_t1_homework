insert into
  roles (id, name, created_by, created_date)
values
  (
    '550e8400-e29b-41d4-a716-446655440000',
    'ROLE_ADMIN',
    'admin',
    NOW ()
  ),
  (
    'f47ac10b-58cc-4372-9502-b30989a18334',
    'ROLE_PREMIUM_USER',
    'admin',
    NOW ()
  ),
  (
    '6ba7b810-9dad-11d1-80b4-00c04fd430c8',
    'ROLE_GUEST',
    'admin',
    NOW ()
  );
