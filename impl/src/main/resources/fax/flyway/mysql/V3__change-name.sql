ALTER TABLE fax_fax CHANGE `_terminated` send_terminated bit(1) NOT NULL DEFAULT b'0';
ALTER TABLE fax_fax CHANGE `_failed` send_failed bit(1) NOT NULL DEFAULT b'0';
