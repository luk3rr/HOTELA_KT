Enum partner_status {
  active
  inactive
}

Table partner {
  id uuid [pk]
  name varchar(255)
  cnpj varchar(255)
  email varchar(255) [unique, not null]
  phone varchar(20)
  address varchar(255)
  contact_name varchar(255)
  contact_email varchar(255)
  contact_phone varchar(20)
  contract_signed boolean
  status partner_status
  created_at datetime
  notes text
}

Table hotel {
  id uuid [pk]
  partner_id uuid [ref: > partner.id]
  name varchar(255)
  address varchar(255)
  city varchar(100)
  state varchar(50)
  zip_code varchar(20)
  phone varchar(20)
  rating decimal(1,1)
  description text
  website varchar(255)
  latitude decimal(10,6)
  longitude decimal(10,6)
}

Table room {
  id uuid [pk]
  hotel_id uuid [ref: > hotel.id]
  number varchar(10)
  floor int
  type varchar(50)
  price decimal(10,2)
  capacity int
  status int
  description text
}

Table customer {
  id uuid [pk]
  name varchar(255)
  email varchar(255) [unique, not null]
  phone varchar(20)
  id_document varchar(50)
  birth_date date
  address varchar(255)
}


Table booking {
  id uuid [pk]
  customer_id uuid [ref: > customer.id]
  room_id uuid [ref: > room.id]
  booked_at datetime
  checkin_date date
  checkout_date date
  guest_count int
  status int
  notes text
}

Table payment {
  id uuid [pk]
  booking_id uuid [ref: > booking.id]
  transaction_id varchar(100)
  amount decimal(10,2)
  payment_method int
  status int
  paid_at datetime
}

Table review {
  id uuid [pk]
  customer_id uuid [ref: > customer.id]
  hotel_id uuid [ref: > hotel.id]
  room_id uuid [ref: > room.id]
  rating int
  comment text
  reviewed_at datetime
}

Table customer_auth {
  id uuid [pk]
  customer_id uuid [unique, ref: > customer.id]
  email varchar(255) [unique, not null]
  password_hash varchar(255) [not null]
  password_salt varchar(255) [not null]
  created_at datetime
  last_login datetime
  active boolean [default: true]
}

Table partner_auth {
  id uuid [pk]
  partner_id uuid [unique, ref: > partner.id]
  email varchar(255) [unique, not null]
  password_hash varchar(255) [not null]
  password_salt varchar(255) [not null]
  created_at datetime
  last_login datetime
  active boolean [default: true]
}
