package tads.eaj.ufrn.roupaapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Roupa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotBlank(message = Message.ERRO_STRING_VAZIA)
    String descricao;
    @DecimalMin(value = "0.05", message = Message.ERRO_VALOR_MINIMO)
    double preco;
    @Min(value = 1, message = Message.ERRO_VALOR_MINIMO)
    int quantidade;
    @NotBlank(message = Message.ERRO_STRING_VAZIA)
    String tipo;
    Date excluido;
    String imageURI;
}
