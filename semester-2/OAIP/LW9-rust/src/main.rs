const _A:   f64 = -2.;
const _B:   f64 = 5.;
const _EPS: f64 = 0.001;

fn function(param: f64) -> f64 {
    param.powf(3.) - 5. * param.powf(2.) + 12.
}

fn derivative(param: f64) -> f64 {
    3. * param.powf(2.) - 10. * param
}

fn find_root(near: f64) -> f64 {

    if function(near).abs() < _EPS {
        return near
    }

    find_root(
        near - ( function(near) / derivative(near) )
    )

}

fn rount_to_eps(f: f64) -> f64 {
    (f / _EPS).round() * _EPS
}

fn main() {
    
    let mut roots: Vec<f64> = Vec::new();

    let mut i = _A;
    while i <= _B {

        let root = rount_to_eps(find_root(i));

        if !roots.contains(&root) {
            roots.push(root);
        }

        i += _EPS;
    }

    println!("Roots: {:?}", roots);

}
