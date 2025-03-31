INSERT INTO public.posts ("views",created_at,updated_at,id,"content",post_status,title) VALUES
	 (NULL,'2025-03-31 23:15:13.105789','2025-03-31 23:15:13.105806','2af70a86-f7c2-47ad-8aec-4737882cc874'::uuid,'The Content here','DRAFT','Hello'),
	 (NULL,'2025-03-31 23:15:35.74033','2025-03-31 23:15:35.740358','ab0d2d17-4a6e-44ec-a225-2ca014994310'::uuid,'The Content here','DRAFT','Hello'),
	 (NULL,'2025-03-31 23:15:43.856123','2025-03-31 23:15:43.856147','e0f2bf1f-7b82-4fdd-8658-24854146ef0d'::uuid,'The Content here','DRAFT','Hello');

INSERT INTO public."comments" (created_at,updated_at,id,post_entity_id,"content") VALUES
	 ('2025-03-31 23:16:09.819442','2025-03-31 23:16:09.819453','cfeec153-c9e8-45ad-af98-13e8085d0d57'::uuid,'e0f2bf1f-7b82-4fdd-8658-24854146ef0d'::uuid,'The Comment Content here'),
	 ('2025-03-31 23:16:10.42847','2025-03-31 23:16:10.42848','e9633026-2f8d-471f-95bb-3ece83ba37ee'::uuid,'e0f2bf1f-7b82-4fdd-8658-24854146ef0d'::uuid,'The Comment Content here'),
	 ('2025-03-31 23:16:31.108494','2025-03-31 23:16:31.108502','a006b32b-7ba7-4573-9a58-559f76b0a176'::uuid,'ab0d2d17-4a6e-44ec-a225-2ca014994310'::uuid,'The Comment Content here'),
	 ('2025-03-31 23:16:31.551255','2025-03-31 23:16:31.551264','63b06f25-a0d9-4264-92fd-4cb0ce95b7ee'::uuid,'ab0d2d17-4a6e-44ec-a225-2ca014994310'::uuid,'The Comment Content here'),
	 ('2025-03-31 23:16:31.938954','2025-03-31 23:16:31.938964','66217320-fd30-487f-92ed-126ae64d0520'::uuid,'ab0d2d17-4a6e-44ec-a225-2ca014994310'::uuid,'The Comment Content here'),
	 ('2025-03-31 23:16:32.324387','2025-03-31 23:16:32.324396','c50eafc3-c792-46f8-b699-b7fadae4bfcf'::uuid,'ab0d2d17-4a6e-44ec-a225-2ca014994310'::uuid,'The Comment Content here'),
	 ('2025-03-31 23:16:53.501507','2025-03-31 23:16:53.501519','db1e69c7-dfa0-4295-8c87-628a2404fd89'::uuid,'2af70a86-f7c2-47ad-8aec-4737882cc874'::uuid,'The Comment Content here'),
	 ('2025-03-31 23:16:53.745289','2025-03-31 23:16:53.745308','0132534c-c8a3-433e-8310-d8d2eb449f29'::uuid,'2af70a86-f7c2-47ad-8aec-4737882cc874'::uuid,'The Comment Content here'),
	 ('2025-03-31 23:16:53.994332','2025-03-31 23:16:53.994342','74f1b690-adb1-46ab-b3b5-5757c7653520'::uuid,'2af70a86-f7c2-47ad-8aec-4737882cc874'::uuid,'The Comment Content here'),
	 ('2025-03-31 23:16:54.236548','2025-03-31 23:16:54.236559','42134727-5ba4-4353-a30f-d594e301f0ab'::uuid,'2af70a86-f7c2-47ad-8aec-4737882cc874'::uuid,'The Comment Content here');
INSERT INTO public."comments" (created_at,updated_at,id,post_entity_id,"content") VALUES
	 ('2025-03-31 23:16:54.534408','2025-03-31 23:16:54.534426','aff19398-a9b0-4f24-b9a7-cb3e07ad0a06'::uuid,'2af70a86-f7c2-47ad-8aec-4737882cc874'::uuid,'The Comment Content here');

