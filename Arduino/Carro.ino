int estado;
int abrirVidro = 2; //aciona a porta 2 do arduino
int fechaVidro = 3;//aciona a porta 3 do arduino

void setup() {

  Serial.begin(9600); //define a taxa de dados em bits por segundos
  pinMode(abrirVidro, OUTPUT); //definição porta 2 como saida
  pinMode(fechaVidro, OUTPUT); //definição porta 2 como saida
}

void loop() {

  if(Serial.available()) //obtem o numero de bytes (caracteres) disponiveis para leitura
  {
      estado = Serial.read(); //ler dados que estejam entrando pela porta serial  
    }
  
  if (estado == 49) //recebe numero 1 em ascii da tabela do arduino
  {  
     vidroCarro(abrirVidro); //chama a função para abrir o vidro
    }
  if (estado == 50) //recebe numero 1 em ascii da tabela do arduino
  {
     fecharVidro(fechaVidro); //chama função para fechar o vidro
    }
  estado = 0;
}

void vidroCarro(int abrirVidro){
  
    digitalWrite(abrirVidro, HIGH); //seta a porta 2 em estado alto
    Serial.println("Vidro Aberto");;
    delay(100);
    digitalWrite(abrirVidro, LOW); //seta a porta 2 em estado baixo
}

void fecharVidro(int fechaVidro){
 
    digitalWrite(fechaVidro, HIGH); //seta a porta 3 em estado alto
    Serial.println("Porta Fechada");
    delay(100);
    digitalWrite(fechaVidro, LOW); //seta a porta 3 em estado baixo
}
  

