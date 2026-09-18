DROP TABLE IF EXISTS public.message_attachments CASCADE;
DROP TABLE IF EXISTS public.messages CASCADE;
DROP TABLE IF EXISTS public.read_statuses CASCADE;
DROP TABLE IF EXISTS public.channels CASCADE;
DROP TABLE IF EXISTS public.user_statuses CASCADE;
DROP TABLE IF EXISTS public.users CASCADE;
DROP TABLE IF EXISTS public.binary_contents CASCADE;


CREATE TABLE public.binary_contents
(
    id         uuid         NOT NULL,
    created_at timestamptz  NOT NULL,
    file_name  varchar(255) NOT NULL,
    size       bigint       NOT NULL,
    bytes      bytea        NOT NULL,
    content_type varchar(100) NOT NULL ,
    CONSTRAINT binary_contents_pk PRIMARY KEY (id)
);

CREATE TABLE public.users
(
    id         uuid         NOT NULL,
    created_at timestamptz  NOT NULL,
    updated_at timestamptz,
    username   varchar(50)  NOT NULL UNIQUE,
    email      varchar(100) NOT NULL UNIQUE,
    "password" varchar(60)  NOT NULL,
    profile_id uuid UNIQUE,
    CONSTRAINT users_pk PRIMARY KEY (id),
    CONSTRAINT profile_pk FOREIGN KEY (profile_id) REFERENCES binary_contents (id) ON DELETE SET NULL
);

CREATE TABLE public.user_statuses
(
    id             uuid        NOT NULL,
    created_at     timestamptz NOT NULL,
    updated_at     timestamptz,
    last_active_at timestamptz NOT NULL,
    user_id        uuid UNIQUE NOT NULL,
    CONSTRAINT user_statuses_pk PRIMARY KEY (id),
    CONSTRAINT users_pk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE public.channels
(
    id          uuid        NOT NULL,
    created_at  timestamptz NOT NULL,
    updated_at  timestamptz,
    name        varchar(100),
    description varchar(500),
    type        varchar(10) NOT NULL,
    CONSTRAINT channels_pk PRIMARY KEY (id),
    CONSTRAINT channels_type_check CHECK ( type IN ('PUBLIC', 'PRIVATE') )
);

CREATE TABLE public.read_statuses
(
    id           uuid        NOT NULL,
    created_at   timestamptz NOT NULL,
    updated_at   timestamptz,
    last_read_at timestamptz NOT NULL,
    user_id      uuid        NOT NULL,
    channel_id   uuid        NOT NULL,
    CONSTRAINT users_pk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT channels_pk FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE,
    UNIQUE (user_id, channel_id)
);

CREATE TABLE public.messages
(
    id         uuid        NOT NULL,
    created_at timestamptz NOT NULL,
    updated_at timestamptz,
    content    text,
    author_id  uuid,
    channel_id uuid        NOT NULL,
    CONSTRAINT messages_pk PRIMARY KEY (id),
    CONSTRAINT users_pk FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE SET NULL,
    CONSTRAINT channels_pk FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE
);

CREATE TABLE public.message_attachments
(
    message_id    uuid NOT NULL,
    attachment_id uuid NOT NULL,
    CONSTRAINT messages_pk FOREIGN KEY (message_id) REFERENCES messages (id) ON DELETE CASCADE,
    CONSTRAINT binary_contents_pk FOREIGN KEY (attachment_id) REFERENCES binary_contents (id) ON DELETE CASCADE
);
